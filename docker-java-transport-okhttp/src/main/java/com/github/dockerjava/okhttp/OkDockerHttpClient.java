package com.github.dockerjava.okhttp;

import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.SSLConfig;
import okhttp3.ConnectionPool;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class OkDockerHttpClient implements DockerHttpClient {

    public static final class Builder {

        private URI dockerHost = null;

        private SSLConfig sslConfig = null;

        private Integer readTimeout = null;

        private Integer connectTimeout = null;

        private Boolean retryOnConnectionFailure = null;

        public Builder dockerHost(URI value) {
            this.dockerHost = Objects.requireNonNull(value, "dockerHost");
            return this;
        }

        public Builder sslConfig(SSLConfig value) {
            this.sslConfig = value;
            return this;
        }

        public Builder readTimeout(Integer value) {
            this.readTimeout = value;
            return this;
        }

        public Builder connectTimeout(Integer value) {
            this.connectTimeout = value;
            return this;
        }

        Builder retryOnConnectionFailure(Boolean value) {
            this.retryOnConnectionFailure = value;
            return this;
        }

        public OkDockerHttpClient build() {
            Objects.requireNonNull(dockerHost, "dockerHost");
            return new OkDockerHttpClient(
                dockerHost,
                sslConfig,
                readTimeout,
                connectTimeout,
                retryOnConnectionFailure
            );
        }
    }

    private static final String SOCKET_SUFFIX = ".socket";

    final OkHttpClient client;

    final OkHttpClient streamingClient;

    private final HttpUrl baseUrl;

    private OkDockerHttpClient(
        URI dockerHost,
        SSLConfig sslConfig,
        Integer readTimeout,
        Integer connectTimeout,
        Boolean retryOnConnectionFailure
    ) {
        okhttp3.OkHttpClient.Builder clientBuilder = new okhttp3.OkHttpClient.Builder()
            .addNetworkInterceptor(new HijackingInterceptor())
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true);

        if (readTimeout != null) {
            clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        }

        if (connectTimeout != null) {
            clientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        }

        if (retryOnConnectionFailure != null) {
            clientBuilder.retryOnConnectionFailure(retryOnConnectionFailure);
        }

        switch (dockerHost.getScheme()) {
            case "unix":
            case "npipe":
                String socketPath = dockerHost.getPath();

                if ("unix".equals(dockerHost.getScheme())) {
                    clientBuilder.socketFactory(new UnixSocketFactory(socketPath));
                } else {
                    clientBuilder.socketFactory(new NamedPipeSocketFactory(socketPath));
                }

                clientBuilder
                    .connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS))
                    .dns(hostname -> {
                        if (hostname.endsWith(SOCKET_SUFFIX)) {
                            return Collections.singletonList(InetAddress.getByAddress(hostname, new byte[]{0, 0, 0, 0}));
                        } else {
                            return Dns.SYSTEM.lookup(hostname);
                        }
                    });
                break;
            default:
        }

        boolean isSSL = false;
        if (sslConfig != null) {
            try {
                SSLContext sslContext = sslConfig.getSSLContext();
                if (sslContext != null) {
                    isSSL = true;
                    clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), new TrustAllX509TrustManager());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        client = clientBuilder.build();

        streamingClient = client.newBuilder().build();

        HttpUrl.Builder baseUrlBuilder;

        switch (dockerHost.getScheme()) {
            case "unix":
            case "npipe":
                baseUrlBuilder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("docker" + SOCKET_SUFFIX);
                break;
            case "tcp":
                baseUrlBuilder = new HttpUrl.Builder()
                    .scheme(isSSL ? "https" : "http")
                    .host(dockerHost.getHost())
                    .port(dockerHost.getPort());
                break;
            default:
                baseUrlBuilder = HttpUrl.get(dockerHost.toString()).newBuilder();
        }
        baseUrl = baseUrlBuilder.build();
    }

    private RequestBody toRequestBody(Request request) {
        InputStream body = request.body();
        if (body != null) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    sink.writeAll(Okio.source(body));
                }
            };
        }
        switch (request.method()) {
            case "POST":
                return RequestBody.create(null, "");
            default:
                return null;
        }
    }

    @Override
    public Response execute(Request request) {
        String url = baseUrl.toString();
        if (url.endsWith("/") && request.path().startsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder()
            .url(url + request.path())
            .tag(Request.class, request)
            .method(request.method(), toRequestBody(request));

        request.headers().forEach(requestBuilder::header);

        final OkHttpClient clientToUse;

        if (request.hijackedInput() == null) {
            clientToUse = client;
        } else {
            clientToUse = streamingClient;
        }

        try {
            return new OkResponse(clientToUse.newCall(requestBuilder.build()).execute());
        } catch (IOException e) {
            throw new UncheckedIOException("Error while executing " + request, e);
        }
    }

    @Override
    public void close() throws IOException {
        for (OkHttpClient clientToClose : new OkHttpClient[]{client, streamingClient}) {
            clientToClose.dispatcher().cancelAll();
            clientToClose.dispatcher().executorService().shutdown();
            clientToClose.connectionPool().evictAll();
        }
    }

    static class OkResponse implements Response {

        static final ThreadLocal<Boolean> CLOSING = ThreadLocal.withInitial(() -> false);

        private final okhttp3.Response response;

        OkResponse(okhttp3.Response response) {
            this.response = response;
        }

        @Override
        public int getStatusCode() {
            return response.code();
        }

        @Override
        public Map<String, List<String>> getHeaders() {
            return response.headers().toMultimap();
        }

        @Override
        public InputStream getBody() {
            ResponseBody body = response.body();
            if (body == null) {
                return null;
            }

            return body.source().inputStream();
        }

        @Override
        public void close() {
            boolean previous = CLOSING.get();
            CLOSING.set(true);
            try {
                response.close();
            } finally {
                CLOSING.set(previous);
            }
        }
    }

    static class TrustAllX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
