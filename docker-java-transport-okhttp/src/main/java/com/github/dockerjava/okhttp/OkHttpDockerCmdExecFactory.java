package com.github.dockerjava.okhttp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.AbstractDockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.SSLConfig;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MultimapBuilder;
import okhttp3.ConnectionPool;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class OkHttpDockerCmdExecFactory extends AbstractDockerCmdExecFactory {

    private static final String SOCKET_SUFFIX = ".socket";

    private ObjectMapper objectMapper;

    private OkHttpClient okHttpClient;

    private HttpUrl baseUrl;

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        super.init(dockerClientConfig);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true);

        URI dockerHost = dockerClientConfig.getDockerHost();
        switch (dockerHost.getScheme()) {
            case "unix":
            case "npipe":
                String socketPath = dockerHost.getPath();

                if ("unix".equals(dockerHost.getScheme())) {
                    clientBuilder
                        .socketFactory(new UnixSocketFactory(socketPath));
                } else {
                    clientBuilder
                        .socketFactory(new NamedPipeSocketFactory(socketPath));
                }

                clientBuilder
                    // Disable pooling
                    .connectionPool(new ConnectionPool(0, 1, TimeUnit.SECONDS))
                    .dns(hostname -> {
                        if (hostname.endsWith(SOCKET_SUFFIX)) {
                            return Collections.singletonList(InetAddress.getByAddress(hostname, new byte[]{0, 0, 0, 0}));
                        } else {
                            return Dns.SYSTEM.lookup(hostname);
                        }
                    });
            default:
        }

        SSLConfig sslConfig = dockerClientConfig.getSSLConfig();
        boolean isSSL = false;
        if (sslConfig != null) {
            try {
                SSLContext sslContext = sslConfig.getSSLContext();
                if (sslContext != null) {
                    isSSL = true;
                    clientBuilder
                            .sslSocketFactory(sslContext.getSocketFactory(), new TrustAllX509TrustManager());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        okHttpClient = clientBuilder.build();

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

        objectMapper = dockerClientConfig.getObjectMapper();
    }

    @Override
    protected OkHttpWebTarget getBaseResource() {
        return new OkHttpWebTarget(
            objectMapper,
            okHttpClient,
            baseUrl,
            ImmutableList.of(),
            MultimapBuilder.hashKeys().hashSetValues().build()
        );
    }

    @Override
    public void close() throws IOException {

    }

    private static class TrustAllX509TrustManager implements X509TrustManager {
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
