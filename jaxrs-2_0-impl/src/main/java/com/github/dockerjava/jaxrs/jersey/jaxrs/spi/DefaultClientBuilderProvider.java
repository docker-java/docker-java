package com.github.dockerjava.jaxrs.jersey.jaxrs.spi;

import com.github.dockerjava.api.DockerClientConfig;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;

/**
 * Created by cruffalo on 11/2/15.
 */
public class DefaultClientBuilderProvider implements ClientBuilderProvider {

    private SSLContext sslContext;

    private Integer readTimeout;

    private Integer connectTimeout;

    private Integer maxTotalConnections;

    private Integer maxPerRouteConnections;

    private ClientRequestFilter[] clientRequestFilters;

    private ClientResponseFilter[] clientResponseFilters;

    @Override
    public ClientBuilder getClientBuilder(final DockerClientConfig dockerClientConfig) {
        return ClientBuilder.newBuilder();
    }

    public SSLContext getSslContext() {
        return sslContext;
    }

    @Override
    public void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    @Override
    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public Integer getMaxTotalConnections() {
        return maxTotalConnections;
    }

    @Override
    public void setMaxTotalConnections(Integer maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public Integer getMaxPerRouteConnections() {
        return maxPerRouteConnections;
    }

    @Override
    public void setMaxPerRouteConnections(Integer maxPerRouteConnections) {
        this.maxPerRouteConnections = maxPerRouteConnections;
    }

    public ClientRequestFilter[] getClientRequestFilters() {
        return clientRequestFilters;
    }

    @Override
    public void setClientRequestFilters(ClientRequestFilter[] clientRequestFilters) {
        this.clientRequestFilters = clientRequestFilters;
    }

    public ClientResponseFilter[] getClientResponseFilters() {
        return clientResponseFilters;
    }

    @Override
    public void setClientResponseFilters(ClientResponseFilter[] clientResponseFilters) {
        this.clientResponseFilters = clientResponseFilters;
    }


}
