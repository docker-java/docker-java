package com.github.dockerjava.httpclient5;

import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerHttpClient;
import com.github.dockerjava.core.SSLConfig;
import com.github.dockerjava.transport.common.NamedPipeSocketFactory;
import com.github.dockerjava.transport.common.UnixSocketFactory;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.ManagedHttpClientConnectionFactory;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentLengthStrategy;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.http.impl.DefaultContentLengthStrategy;
import org.apache.hc.core5.http.impl.io.EmptyInputStream;
import org.apache.hc.core5.http.io.entity.InputStreamEntity;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.net.URIAuthority;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ApacheDockerHttpClient implements DockerHttpClient {

    public static final class Factory {

        private DockerClientConfig dockerClientConfig = null;

        public Factory dockerClientConfig(DockerClientConfig value) {
            this.dockerClientConfig = value;
            return this;
        }

        public ApacheDockerHttpClient build() {
            Objects.requireNonNull(dockerClientConfig, "dockerClientConfig");
            return new ApacheDockerHttpClient(dockerClientConfig);
        }
    }

    private final CloseableHttpClient httpClient;

    private final HttpHost host;

    private ApacheDockerHttpClient(DockerClientConfig dockerClientConfig) {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = createConnectionSocketFactoryRegistry(dockerClientConfig);

        URI dockerHost = dockerClientConfig.getDockerHost();

        switch (dockerHost.getScheme()) {
            case "unix":
            case "npipe":
                host = new HttpHost(dockerHost.getScheme(), "localhost", 2375);
                break;
            case "tcp":
                host = new HttpHost(
                    socketFactoryRegistry.lookup("https") != null ? "https" : "http",
                    dockerHost.getHost(),
                    dockerHost.getPort()
                );
                break;
            default:
                host = HttpHost.create(dockerHost);
        }

        httpClient = HttpClients.custom()
            .setRequestExecutor(new HijackingHttpRequestExecutor(null))
            .setConnectionManager(new PoolingHttpClientConnectionManager(
                socketFactoryRegistry,
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
            ))
            .build();
    }

    private Registry<ConnectionSocketFactory> createConnectionSocketFactoryRegistry(DockerClientConfig dockerClientConfig) {
        RegistryBuilder<ConnectionSocketFactory> socketFactoryRegistryBuilder = RegistryBuilder.create();

        SSLConfig sslConfig = dockerClientConfig.getSSLConfig();
        if (sslConfig != null) {
            try {
                SSLContext sslContext = sslConfig.getSSLContext();
                if (sslContext != null) {
                    socketFactoryRegistryBuilder.register("https", new SSLConnectionSocketFactory(sslContext));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return socketFactoryRegistryBuilder
            .register("tcp", PlainConnectionSocketFactory.INSTANCE)
            .register("http", PlainConnectionSocketFactory.INSTANCE)
            .register("unix", new PlainConnectionSocketFactory() {
                @Override
                public Socket createSocket(HttpContext context) {
                    URI dockerHost = dockerClientConfig.getDockerHost();

                    return new UnixSocketFactory(dockerHost.getPath()).createSocket();
                }
            })
            .register("npipe", new PlainConnectionSocketFactory() {
                @Override
                public Socket createSocket(HttpContext context) {
                    URI dockerHost = dockerClientConfig.getDockerHost();

                    return new NamedPipeSocketFactory(dockerHost.getPath()).createSocket();
                }
            })
            .build();
    }

    @Override
    public Response execute(Request request) {
        HttpContext context = new BasicHttpContext();
        HttpUriRequestBase httpUriRequest = new HttpUriRequestBase(request.method(), URI.create(request.path()));
        httpUriRequest.setScheme(host.getSchemeName());
        httpUriRequest.setAuthority(new URIAuthority(host.getHostName(), host.getPort()));

        request.headers().forEach(httpUriRequest::addHeader);

        InputStream body = request.body();
        if (body != null) {
            httpUriRequest.setEntity(new InputStreamEntity(body, null));
        }

        if (request.hijackedInput() != null) {
            context.setAttribute(HijackingHttpRequestExecutor.HIJACKED_INPUT_ATTRIBUTE, request.hijackedInput());
            httpUriRequest.setHeader("Upgrade", "tcp");
            httpUriRequest.setHeader("Connection", "Upgrade");
        }

        try {
            CloseableHttpResponse response = httpClient.execute(host, httpUriRequest, context);

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

        private final HttpUriRequestBase request;

        private final CloseableHttpResponse response;

        ApacheResponse(HttpUriRequestBase httpUriRequest, CloseableHttpResponse response) {
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
            request.abort();
        }
    }
}
