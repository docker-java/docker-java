package com.github.dockerjava.jsch;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerHttpClient;
import com.github.dockerjava.core.SSLConfig;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.OpenSSHConfig;
import com.jcraft.jsch.Session;
import okhttp3.ConnectionPool;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.File;
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

public final class SsshWithOKDockerHttpClient implements DockerHttpClient {

    public static final class Factory {

        private DockerClientConfig dockerClientConfig = null;

        private Integer readTimeout = null;

        private Integer connectTimeout = null;

        private Boolean retryOnConnectionFailure = null;

        final SSHDockerConfig sshDockerConfig = new SSHDockerConfig();

        public Factory dockerClientConfig(DockerClientConfig value) {
            this.dockerClientConfig = value;
            return this;
        }

        public Factory readTimeout(Integer value) {
            this.readTimeout = value;
            return this;
        }

        public Factory connectTimeout(Integer value) {
            this.connectTimeout = value;
            return this;
        }

        Factory retryOnConnectionFailure(Boolean value) {
            this.retryOnConnectionFailure = value;
            return this;
        }

        /**
         * use socket and overwrite default socket path {@link SSHDockerConfig#VAR_RUN_DOCKER_SOCK}
         *
         * @param socketPath
         * @return
         */
        public Factory useSocket(String socketPath) {
            this.sshDockerConfig.setUseSocket(true);
            this.sshDockerConfig.setSocketPath(socketPath);
            return this;
        }

        /**
         * pass {@link Session} if already connected
         *
         * @param session
         * @return
         */
        public Factory sshSession(Session session) {
            this.sshDockerConfig.setSession(session);
            return this;
        }

        /**
         * set identityFile for public key authentication
         *
         * @param identityFile
         * @return
         */
        public Factory identityFile(File identityFile) {
            this.sshDockerConfig.setIdentityFile(identityFile);
            return this;
        }

        /**
         * set identityFile from ~/.ssh/ folder for public key authentication
         *
         * @param privateKey private key filename
         * @return
         */
        public Factory identity(String privateKey) {
            return identityFile(new File(System.getProperty("user.home") + File.separator + ".ssh" + File.separator + privateKey));
        }

        public Factory interceptor(Interceptor interceptor) {
            this.sshDockerConfig.setInterceptor(interceptor);
            return this;
        }

        public Factory useSocket() {
            this.sshDockerConfig.setUseSocket(true);
            return this;
        }

        public Factory useSocat() {
            this.sshDockerConfig.setUseSocat(true);
            return this;
        }

        public Factory useTcp() {
            this.sshDockerConfig.setUseTcp(true);
            return this;
        }

        public Factory useTcp(int port) {
            this.sshDockerConfig.setUseTcp(true);
            this.sshDockerConfig.setTcpPort(port);
            return this;
        }

        public SsshWithOKDockerHttpClient build() throws IOException, JSchException {
            Objects.requireNonNull(dockerClientConfig, "dockerClientConfig not provided");
            return new SsshWithOKDockerHttpClient(
                dockerClientConfig,
                readTimeout,
                connectTimeout,
                retryOnConnectionFailure,
                sshDockerConfig);
        }
    }

    private static final String SOCKET_SUFFIX = ".socket";

    final OkHttpClient client;

    final OkHttpClient streamingClient;

    private HttpUrl baseUrl;

    private Session session;
    private boolean externalSession = false;
    private final File identityFile;

    private SsshWithOKDockerHttpClient(
        DockerClientConfig dockerClientConfig,
        Integer readTimeout,
        Integer connectTimeout,
        Boolean retryOnConnectionFailure,
        SSHDockerConfig sshDockerConfig) throws IOException, JSchException {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            //.addNetworkInterceptor(new HijackingInterceptor())
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true);

        if (sshDockerConfig.getInterceptor() != null) {
            clientBuilder.addInterceptor(sshDockerConfig.getInterceptor());
        }

        if (readTimeout != null) {
            clientBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        }

        if (connectTimeout != null) {
            clientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        }

        if (retryOnConnectionFailure != null) {
            clientBuilder.retryOnConnectionFailure(retryOnConnectionFailure);
        }

        this.session = sshDockerConfig.getSession();
        this.externalSession = sshDockerConfig.getSession() != null;
        this.identityFile = sshDockerConfig.getIdentityFile();

        URI dockerHost = dockerClientConfig.getDockerHost();

        if ("ssh".equals(dockerHost.getScheme())) {

            this.session = connectSSH(dockerHost, connectTimeout != null ? connectTimeout : 0);

            final SSHSocketFactory socketFactory = new SSHSocketFactory(session, sshDockerConfig);

            clientBuilder.socketFactory(socketFactory);

            clientBuilder
                .connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS))
                .dns(hostname -> {
                    if (hostname.endsWith(SOCKET_SUFFIX)) {
                        return Collections.singletonList(InetAddress.getByAddress(hostname, new byte[]{0, 0, 0, 0}));
                    } else {
                        return Dns.SYSTEM.lookup(hostname);
                    }
                });
        } else {
            throw new IllegalArgumentException("this implementation only supports ssh connection scheme.");
        }

        boolean isSSL = false;
        SSLConfig sslConfig = dockerClientConfig.getSSLConfig();
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

        initBaseUrl(dockerHost, isSSL);
    }

    private void initBaseUrl(URI dockerHost, boolean isSSL) {
        HttpUrl.Builder baseUrlBuilder;

        switch (dockerHost.getScheme()) {
            case "ssh":
                // der http Host und Port spielen keine Rolle, da das Socket geforwarded wird....
                baseUrlBuilder = new HttpUrl.Builder()
                    .scheme("http")
                    .host("127.0.0.1");
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
        disconnectSSH();
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

    private Session connectSSH(URI connectionString, int connectTimeout) throws IOException, JSchException {

        if (session != null && session.isConnected()) {
            return session;
        }

        final JSch jSch = new JSch();
        JSch.setLogger(new JschLogger());

        final String configFile = System.getProperty("user.home") + File.separator + ".ssh" + File.separator + "config";
        final File file = new File(configFile);
        if (file.exists()) {
            final OpenSSHConfig openSSHConfig = OpenSSHConfig.parseFile(file.getAbsolutePath());
            jSch.setConfigRepository(openSSHConfig);
        }

        final int port = connectionString.getPort() > 0 ? connectionString.getPort() : 22;
        final Session newSession = jSch.getSession(connectionString.getUserInfo(), connectionString.getHost(), port);

        newSession.setConfig("StrictHostKeyChecking", "no");
        // https://stackoverflow.com/questions/10881981/sftp-connection-through-java-asking-for-weird-authentication
        newSession.setConfig("PreferredAuthentications", "publickey");

        if (identityFile != null) {
            jSch.addIdentity(identityFile.getAbsolutePath());
        }

        newSession.connect(connectTimeout);

        return newSession;
    }


    private void disconnectSSH() {
        if (!externalSession) {
            session.disconnect();
        }
    }
}
