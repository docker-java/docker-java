package com.github.dockerjava.jaxrs.jersey.jaxrs.spi;

import com.github.dockerjava.api.DockerClientConfig;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;

/**
 * Created by cruffalo on 11/2/15.
 */
public interface ClientBuilderProvider {

    ClientBuilder getClientBuilder(final DockerClientConfig dockerClientConfig);

    void setSslContext(final SSLContext sslContext);

    void setReadTimeout(final Integer readTimeout);

    void setConnectTimeout(final Integer connectTimeout);

    void setMaxTotalConnections(final Integer maxTotalConnections);

    void setMaxPerRouteConnections(final Integer maxPerRouteConnections);

    void setClientRequestFilters(final ClientRequestFilter[] clientRequestFilters);

    void setClientResponseFilters(final ClientResponseFilter[] clientResponseFilters);

}
