package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.command.DelegatingDockerCmdExecFactory;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import org.glassfish.jersey.client.RequestEntityProcessing;

import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseFilter;

//import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
// see https://github.com/docker-java/docker-java/issues/196
/**
 * @deprecated use {@link JerseyDockerHttpClient} with {@link DockerClientImpl#getInstance(DockerClientConfig, DockerHttpClient)}
 */
@Deprecated
public class JerseyDockerCmdExecFactory extends DelegatingDockerCmdExecFactory implements DockerClientConfigAware {

    private JerseyDockerHttpClient.Builder clientBuilder = new JerseyDockerHttpClient.Builder();

    @Deprecated
    protected Integer connectTimeout;

    @Deprecated
    protected Integer readTimeout;

    private DefaultDockerCmdExecFactory dockerCmdExecFactory;

    @Override
    public final DockerCmdExecFactory getDockerCmdExecFactory() {
        return dockerCmdExecFactory;
    }

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        clientBuilder = clientBuilder
            .dockerHost(dockerClientConfig.getDockerHost())
            .sslConfig(dockerClientConfig.getSSLConfig());
        dockerCmdExecFactory = new DefaultDockerCmdExecFactory(
            clientBuilder.build(),
            dockerClientConfig.getObjectMapper()
        );
        dockerCmdExecFactory.init(dockerClientConfig);
    }

    /**
     * Configure connection timeout in milliseconds
     */
    public JerseyDockerCmdExecFactory withConnectTimeout(Integer connectTimeout) {
        clientBuilder = clientBuilder.connectTimeout(connectTimeout);
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Configure read timeout in milliseconds
     */
    public JerseyDockerCmdExecFactory withReadTimeout(Integer readTimeout) {
        clientBuilder = clientBuilder.readTimeout(readTimeout);
        this.readTimeout = readTimeout;
        return this;
    }

    public JerseyDockerCmdExecFactory withMaxTotalConnections(Integer maxTotalConnections) {
        clientBuilder = clientBuilder.maxTotalConnections(maxTotalConnections);
        return this;
    }

    public JerseyDockerCmdExecFactory withMaxPerRouteConnections(Integer maxPerRouteConnections) {
        clientBuilder = clientBuilder.maxPerRouteConnections(maxPerRouteConnections);
        return this;
    }

    public JerseyDockerCmdExecFactory withConnectionRequestTimeout(Integer connectionRequestTimeout) {
        clientBuilder = clientBuilder.connectionRequestTimeout(connectionRequestTimeout);
        return this;
    }

    public JerseyDockerCmdExecFactory withClientResponseFilters(ClientResponseFilter... clientResponseFilter) {
        clientBuilder = clientBuilder.clientResponseFilters(clientResponseFilter);
        return this;
    }

    public JerseyDockerCmdExecFactory withClientRequestFilters(ClientRequestFilter... clientRequestFilters) {
        clientBuilder = clientBuilder.clientRequestFilters(clientRequestFilters);
        return this;
    }

    public JerseyDockerCmdExecFactory withRequestEntityProcessing(RequestEntityProcessing requestEntityProcessing) {
        clientBuilder = clientBuilder.requestEntityProcessing(requestEntityProcessing);
        return this;
    }
}
