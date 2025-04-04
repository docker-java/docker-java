package com.github.dockerjava.httpclient5;

import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.NamedPipeSocket;
import com.github.dockerjava.transport.SSLConfig;
import com.github.dockerjava.transport.UnixSocket;

import org.apache.hc.client5.http.SystemDefaultDnsResolver;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.io.HttpClientConnectionOperator;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ConnectionClosedException;
import org.apache.hc.core5.http.ContentLengthStrategy;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.io.entity.EmptyInputStream;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.http.protocol.HttpCoreContext;
import org.apache.hc.core5.net.URIAuthority;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ApacheDockerHttpClientImpl implements DockerHttpClient {

    private final CloseableHttpClient httpClient;
    private final HttpHost host;
    private final String pathPrefix;

    protected ApacheDockerHttpClientImpl(
        URI dockerHost,
        SSLConfig sslConfig,
        int maxConnections,
        Duration connectionTimeout,
        Duration responseTimeout
    ) {
        SSLContext sslContext;
        HostnameVerifier hostnameVerifier;
        try {
            sslContext = sslConfig != null ? sslConfig.getSSLContext() : null;
            hostnameVerifier = sslConfig != null ? sslConfig.getHostnameVerifier() : null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        HttpClientConnectionOperator connectionOperator = createConnectionOperator(dockerHost, sslContext, hostnameVerifier);

        switch (dockerHost.getScheme()) {
            case "unix":
            case "npipe":
                pathPrefix = "";
                host = new HttpHost(dockerHost.getScheme(), "localhost", 2375);
                break;
            case "tcp":
                String rawPath = dockerHost.getRawPath();
                pathPrefix = rawPath.endsWith("/")
                    ? rawPath.substring(0, rawPath.length() - 1)
                    : rawPath;
                host = new HttpHost(
                    sslContext != null ? "https" : "http",
                    dockerHost.getHost(),
                    dockerHost.getPort()
                );
                break;
            default:
                throw new IllegalArgumentException("Unsupported protocol scheme: " + dockerHost);
        }

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
            connectionOperator,
            null,
            null,
            null,
            new ManagedHttpClientConnectionFactory(
                null,
                null,
                null,
                null,
                message -> {
                    Header transferEncodingHeader = message.getFirstHeader(HttpHeaders.TRANSFER_ENCODING);
                    if (transferEncodingHeader != null) {
                        if ("identity".equalsIgnoreCase(transferEncodingHeader.getValue())) {
                            return ContentLengthStrategy.UNDEFINED;
                        }
                    }
                    return DefaultContentLengthStrategy.INSTANCE.determineLength(message);
                },
                null
            )
        );
        // See https://github.com/docker-java/docker-java/pull/1590#issuecomment-870581289
        connectionManager.setDefaultSocketConfig(
            SocketConfig.copy(SocketConfig.DEFAULT)
                .setSoTimeout(Timeout.ZERO_MILLISECONDS)
                .build()
        );
        connectionManager.setMaxTotal(maxConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnections);
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom()
            .setValidateAfterInactivity(TimeValue.NEG_ONE_SECOND)
            .setConnectTimeout(connectionTimeout != null ? Timeout.of(connectionTimeout.toNanos(), TimeUnit.NANOSECONDS) : null)
            .build());

        httpClient = HttpClients.custom()
            .setRequestExecutor(new HijackingHttpRequestExecutor(null))
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(RequestConfig.custom()
                .setResponseTimeout(responseTimeout != null ? Timeout.of(responseTimeout.toNanos(), TimeUnit.NANOSECONDS) : null)
                .build())
            .disableConnectionState()
            .build();
    }

    private HttpClientConnectionOperator createConnectionOperator(
        URI dockerHost,
        SSLContext sslContext,
        HostnameVerifier hostnameVerifier
    ) {
        String dockerHostScheme = dockerHost.getScheme();
        String dockerHostPath = dockerHost.getPath();
        TlsSocketStrategy tlsSocketStrategy = sslContext != null ?
            new DefaultClientTlsStrategy(sslContext, hostnameVerifier) : DefaultClientTlsStrategy.createSystemDefault();
        return new DefaultHttpClientConnectionOperator(
            socksProxy -> {
                if ("unix".equalsIgnoreCase(dockerHostScheme)) {
                    return UnixSocket.get(dockerHostPath);
                } else if ("npipe".equalsIgnoreCase(dockerHostScheme)) {
                    return new NamedPipeSocket(dockerHostPath);
                } else {
                    return socksProxy == null ? new Socket() : new Socket(socksProxy);
                }
            },
            DefaultSchemePortResolver.INSTANCE,
            SystemDefaultDnsResolver.INSTANCE,
            name -> "https".equalsIgnoreCase(name) ? tlsSocketStrategy : null);
    }

    @Override
    public Response execute(Request request) {
        HttpContext context = new HttpCoreContext();
        HttpUriRequestBase httpUriRequest = new HttpUriRequestBase(request.method(), URI.create(pathPrefix + request.path()));
        httpUriRequest.setScheme(host.getSchemeName());
        httpUriRequest.setAuthority(new URIAuthority(host.getHostName(), host.getPort()));

        request.headers().forEach(httpUriRequest::addHeader);

        byte[] bodyBytes = request.bodyBytes();
        if (bodyBytes != null) {
            httpUriRequest.setEntity(new ByteArrayEntity(bodyBytes, null));
        } else {
            InputStream body = request.body();
            if (body != null) {
                httpUriRequest.setEntity(new InputStreamEntity(body, null));
            }
        }

        if (request.hijackedInput() != null) {
            context.setAttribute(HijackingHttpRequestExecutor.HIJACKED_INPUT_ATTRIBUTE, request.hijackedInput());
            httpUriRequest.setHeader("Upgrade", "tcp");
            httpUriRequest.setHeader("Connection", "Upgrade");
        }

        try {
            ClassicHttpResponse response = httpClient.executeOpen(host, httpUriRequest, context);

            return new ApacheResponse(httpUriRequest, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

    static class ApacheResponse implements Response {

        private static final Logger LOGGER = LoggerFactory.getLogger(ApacheResponse.class);

        private final HttpUriRequestBase request;

        private final ClassicHttpResponse response;

        ApacheResponse(HttpUriRequestBase httpUriRequest, ClassicHttpResponse response) {
            this.request = httpUriRequest;
            this.response = response;
        }

        @Override
        public int getStatusCode() {
            return response.getCode();
        }

        @Override
        public Map<String, List<String>> getHeaders() {
            return Stream.of(response.getHeaders()).collect(Collectors.groupingBy(
                NameValuePair::getName,
                Collectors.mapping(NameValuePair::getValue, Collectors.toList())
            ));
        }

        @Override
        public String getHeader(String name) {
            Header firstHeader = response.getFirstHeader(name);
            return firstHeader != null ? firstHeader.getValue() : null;
        }

        @Override
        public InputStream getBody() {
            try {
                return response.getEntity() != null
                    ? response.getEntity().getContent()
                    : EmptyInputStream.INSTANCE;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() {
            try {
                request.abort();
            } catch (Exception e) {
                LOGGER.debug("Failed to abort the request", e);
            }

            try {
                response.close();
            } catch (ConnectionClosedException e) {
                LOGGER.trace("Failed to close the response", e);
            } catch (Exception e) {
                LOGGER.debug("Failed to close the response", e);
            }
        }
    }
}
