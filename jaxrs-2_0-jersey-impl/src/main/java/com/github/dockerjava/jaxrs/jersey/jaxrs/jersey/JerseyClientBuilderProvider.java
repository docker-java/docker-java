package com.github.dockerjava.jaxrs.jersey.jaxrs.jersey;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.github.dockerjava.api.DockerClientConfig;
import com.github.dockerjava.jaxrs.jersey.jaxrs.filter.JsonClientFilter;
import com.github.dockerjava.jaxrs.jersey.jaxrs.filter.ResponseStatusExceptionFilter;
import com.github.dockerjava.jaxrs.jersey.jaxrs.filter.SelectiveLoggingFilter;
import com.github.dockerjava.jaxrs.jersey.jaxrs.jersey.connector.ApacheConnectorProvider;
import com.github.dockerjava.jaxrs.jersey.jaxrs.spi.DefaultClientBuilderProvider;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;
import java.net.URI;

/**
 * Created by cruffalo on 11/2/15.
 */
public class JerseyClientBuilderProvider extends DefaultClientBuilderProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JerseyClientBuilderProvider.class.getName());

    @Override
    public ClientBuilder getClientBuilder(final DockerClientConfig dockerClientConfig) {

        final URI originalUri = dockerClientConfig.getUri();

        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new ApacheConnectorProvider());
        clientConfig.property(CommonProperties.FEATURE_AUTO_DISCOVERY_DISABLE, true);

        clientConfig.register(ResponseStatusExceptionFilter.class);
        clientConfig.register(JsonClientFilter.class);
        clientConfig.register(JacksonJsonProvider.class);

        // logging may disabled via log level
        clientConfig.register(new SelectiveLoggingFilter(LOGGER, true));

        if (this.getReadTimeout() != null) {
            clientConfig.property(ClientProperties.READ_TIMEOUT, this.getReadTimeout());
        }

        if (this.getConnectTimeout() != null) {
            clientConfig.property(ClientProperties.CONNECT_TIMEOUT, this.getConnectTimeout());
        }

        if (this.getClientResponseFilters() != null) {
            for (ClientResponseFilter clientResponseFilter : this.getClientResponseFilters()) {
                if (clientResponseFilter != null)
                    clientConfig.register(clientResponseFilter);
            }
        }

        if (this.getClientRequestFilters() != null) {
            for (ClientRequestFilter clientRequestFilter : this.getClientRequestFilters()) {
                if (clientRequestFilter != null)
                    clientConfig.register(clientRequestFilter);
            }
        }

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(getSchemeRegistry(originalUri, this.getSslContext()));

        if (this.getMaxTotalConnections() != null) {
            connManager.setMaxTotal(this.getMaxTotalConnections());
        }
        if (this.getMaxPerRouteConnections() != null) {
            connManager.setDefaultMaxPerRoute(this.getMaxPerRouteConnections());
        }

        // update connector for socket uri if needed
        if (originalUri.getScheme().equals("unix")) {
            dockerClientConfig.setUri(UnixConnectionSocketFactory.sanitizeUri(originalUri));
        }

        clientConfig.property(ApacheClientProperties.CONNECTION_MANAGER, connManager);

        // Configure connection pool timeout
        // clientConfig.property(ApacheClientProperties.REQUEST_CONFIG, RequestConfig.custom()
        // .setConnectionRequestTimeout(1000).build());

        return ClientBuilder.newBuilder().withConfig(clientConfig);
    }

    private org.apache.http.config.Registry<ConnectionSocketFactory> getSchemeRegistry(final URI originalUri, SSLContext sslContext) {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        registryBuilder.register("http", PlainConnectionSocketFactory.getSocketFactory());
        if (sslContext != null) {
            registryBuilder.register("https", new SSLConnectionSocketFactory(sslContext));
        }
        registryBuilder.register("unix", new UnixConnectionSocketFactory(originalUri));
        return registryBuilder.build();
    }

}
