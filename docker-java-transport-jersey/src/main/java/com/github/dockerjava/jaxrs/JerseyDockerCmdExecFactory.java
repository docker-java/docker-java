package com.github.dockerjava.jaxrs;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.AbstractDockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.SSLConfig;
import com.github.dockerjava.jaxrs.filter.JsonClientFilter;
import com.github.dockerjava.jaxrs.filter.ResponseStatusExceptionFilter;
import com.github.dockerjava.jaxrs.filter.SelectiveLoggingFilter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

//import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
// see https://github.com/docker-java/docker-java/issues/196

public class JerseyDockerCmdExecFactory extends AbstractDockerCmdExecFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyDockerCmdExecFactory.class.getName());

    private Client client;

    private JerseyWebTarget baseResource;

    private Integer readTimeout = null;

    private Integer connectTimeout = null;

    private Integer maxTotalConnections = null;

    private Integer maxPerRouteConnections = null;

    private Integer connectionRequestTimeout = null;

    private ClientRequestFilter[] clientRequestFilters = null;

    private ClientResponseFilter[] clientResponseFilters = null;

    private DockerClientConfig dockerClientConfig;

    private PoolingHttpClientConnectionManager connManager = null;

    private RequestEntityProcessing requestEntityProcessing;

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        checkNotNull(dockerClientConfig, "config was not specified");
        this.dockerClientConfig = dockerClientConfig;

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());
        clientConfig.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

        if (requestEntityProcessing != null) {
            clientConfig.property(ClientProperties.REQUEST_ENTITY_PROCESSING, requestEntityProcessing);
        }

        clientConfig.register(new ResponseStatusExceptionFilter(dockerClientConfig.getObjectMapper()));
        clientConfig.register(JsonClientFilter.class);
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

        clientConfig.register(new JacksonJsonProvider(dockerClientConfig.getObjectMapper()));

        // logging may disabled via log level
        clientConfig.register(new SelectiveLoggingFilter(LOGGER, true));

        if (readTimeout != null) {
            requestConfigBuilder.setSocketTimeout(readTimeout);
            clientConfig.property(ClientProperties.READ_TIMEOUT, readTimeout);
        }

        if (connectTimeout != null) {
            requestConfigBuilder.setConnectTimeout(connectTimeout);
            clientConfig.property(ClientProperties.CONNECT_TIMEOUT, connectTimeout);
        }

        if (clientResponseFilters != null) {
            for (ClientResponseFilter clientResponseFilter : clientResponseFilters) {
                if (clientResponseFilter != null) {
                    clientConfig.register(clientResponseFilter);
                }
            }
        }

        if (clientRequestFilters != null) {
            for (ClientRequestFilter clientRequestFilter : clientRequestFilters) {
                if (clientRequestFilter != null) {
                    clientConfig.register(clientRequestFilter);
                }
            }
        }

        URI originalUri = dockerClientConfig.getDockerHost();

        String protocol = null;

        SSLContext sslContext = null;

        try {
            final SSLConfig sslConfig = dockerClientConfig.getSSLConfig();
            if (sslConfig != null) {
                sslContext = sslConfig.getSSLContext();
            }
        } catch (Exception ex) {
            throw new DockerClientException("Error in SSL Configuration", ex);
        }

        if (sslContext != null) {
            protocol = "https";
        } else {
            protocol = "http";
        }

        switch (originalUri.getScheme()) {
            case "unix":
                break;
            case "tcp":
                try {
                    originalUri = new URI(originalUri.toString().replaceFirst("tcp", protocol));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }

                configureProxy(clientConfig, originalUri, protocol);
                break;
            default:
                throw new IllegalArgumentException("Unsupported protocol scheme: " + originalUri);
        }

        connManager = new PoolingHttpClientConnectionManager(getSchemeRegistry(
                originalUri, sslContext)) {

            @Override
            public void close() {
                super.shutdown();
            }

            @Override
            public void shutdown() {
                // Disable shutdown of the pool. This will be done later, when this factory is closed
                // This is a workaround for finalize method on jerseys ClientRuntime which
                // closes the client and shuts down the connection pool when it is garbage collected
            }
        };

        if (maxTotalConnections != null) {
            connManager.setMaxTotal(maxTotalConnections);
        }
        if (maxPerRouteConnections != null) {
            connManager.setDefaultMaxPerRoute(maxPerRouteConnections);
        }

        clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, connManager);

        // Configure connection pool timeout
        if (connectionRequestTimeout != null) {
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeout);
        }
        clientConfig.property(ApacheClientProperties.REQUEST_CONFIG, requestConfigBuilder.build());
        ClientBuilder clientBuilder = ClientBuilder.newBuilder().withConfig(clientConfig);

        if (sslContext != null) {
            clientBuilder.sslContext(sslContext);
        }

        client = clientBuilder.build();

        baseResource = new JerseyWebTarget(
                dockerClientConfig.getObjectMapper(),
                client.target(sanitizeUrl(originalUri).toString())
                        .path(dockerClientConfig.getApiVersion().asWebPathPart())
        );

        super.init(dockerClientConfig);
    }

    private URI sanitizeUrl(URI originalUri) {
        if (originalUri.getScheme().equals("unix")) {
            return UnixConnectionSocketFactory.sanitizeUri(originalUri);
        }
        return originalUri;
    }

    private void configureProxy(ClientConfig clientConfig, URI originalUri, String protocol) {

        List<Proxy> proxies = ProxySelector.getDefault().select(originalUri);

        for (Proxy proxy : proxies) {
            InetSocketAddress address = (InetSocketAddress) proxy.address();
            if (address != null) {
                String hostname = address.getHostName();
                int port = address.getPort();

                clientConfig.property(ClientProperties.PROXY_URI, "http://" + hostname + ":" + port);

                String httpProxyUser = System.getProperty(protocol + ".proxyUser");
                if (httpProxyUser != null) {
                    clientConfig.property(ClientProperties.PROXY_USERNAME, httpProxyUser);
                    String httpProxyPassword = System.getProperty(protocol + ".proxyPassword");
                    if (httpProxyPassword != null) {
                        clientConfig.property(ClientProperties.PROXY_PASSWORD, httpProxyPassword);
                    }
                }
            }
        }
    }

    private org.apache.http.config.Registry<ConnectionSocketFactory> getSchemeRegistry(final URI originalUri,
                                                                                       SSLContext sslContext) {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
        if (sslContext != null) {
            registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext));
        }
        registryBuilder.register("unix", new UnixConnectionSocketFactory(originalUri));
        return registryBuilder.build();
    }

    protected JerseyWebTarget getBaseResource() {
        checkNotNull(baseResource, "Factory not initialized, baseResource not set. You probably forgot to call init()!");
        return baseResource;
    }

    protected DockerClientConfig getDockerClientConfig() {
        checkNotNull(dockerClientConfig,
                "Factor not initialized, dockerClientConfig not set. You probably forgot to call init()!");
        return dockerClientConfig;
    }

    @Override
    public void close() throws IOException {
        checkNotNull(client, "Factory not initialized. You probably forgot to call init()!");
        client.close();
        connManager.close();
    }

    public JerseyDockerCmdExecFactory withReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public JerseyDockerCmdExecFactory withConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public JerseyDockerCmdExecFactory withMaxTotalConnections(Integer maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
        return this;
    }

    public JerseyDockerCmdExecFactory withMaxPerRouteConnections(Integer maxPerRouteConnections) {
        this.maxPerRouteConnections = maxPerRouteConnections;
        return this;
    }

    public JerseyDockerCmdExecFactory withConnectionRequestTimeout(Integer connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public JerseyDockerCmdExecFactory withClientResponseFilters(ClientResponseFilter... clientResponseFilter) {
        this.clientResponseFilters = clientResponseFilter;
        return this;
    }

    public JerseyDockerCmdExecFactory withClientRequestFilters(ClientRequestFilter... clientRequestFilters) {
        this.clientRequestFilters = clientRequestFilters;
        return this;
    }

    public JerseyDockerCmdExecFactory withRequestEntityProcessing(RequestEntityProcessing requestEntityProcessing) {
        this.requestEntityProcessing = requestEntityProcessing;
        return this;
    }

    /**
     * release connections from the pool
     *
     * @param idleSeconds idle seconds, longer than the configured value will be evicted
     */
    public void releaseConnection(long idleSeconds) {
        this.connManager.closeExpiredConnections();
        this.connManager.closeIdleConnections(idleSeconds, TimeUnit.SECONDS);
    }
}
