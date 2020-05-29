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
 * @deprecated use {@link JerseyDockerHttpClient} with {@link DockerClientImpl#withHttpClient(DockerHttpClient)}
 */
@Deprecated
public class JerseyDockerCmdExecFactory extends DelegatingDockerCmdExecFactory implements DockerClientConfigAware {

    private JerseyDockerHttpClient.Factory clientFactory = new JerseyDockerHttpClient.Factory();

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
        clientFactory = clientFactory
            .dockerHost(dockerClientConfig.getDockerHost())
            .sslConfig(dockerClientConfig.getSSLConfig());
        dockerCmdExecFactory = new DefaultDockerCmdExecFactory(
            clientFactory.build(),
            dockerClientConfig.getObjectMapper()
        );
        dockerCmdExecFactory.init(dockerClientConfig);
    }

    /**
     * Configure connection timeout in milliseconds
     */
    public JerseyDockerCmdExecFactory withConnectTimeout(Integer connectTimeout) {
        clientFactory = clientFactory.connectTimeout(connectTimeout);
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Configure read timeout in milliseconds
     */
    public JerseyDockerCmdExecFactory withReadTimeout(Integer readTimeout) {
        clientFactory = clientFactory.readTimeout(readTimeout);
        this.readTimeout = readTimeout;
        return this;
    }

    public JerseyDockerCmdExecFactory withMaxTotalConnections(Integer maxTotalConnections) {
        clientFactory = clientFactory.maxTotalConnections(maxTotalConnections);
        return this;
    }

    public JerseyDockerCmdExecFactory withMaxPerRouteConnections(Integer maxPerRouteConnections) {
        clientFactory = clientFactory.maxPerRouteConnections(maxPerRouteConnections);
        return this;
    }

    public JerseyDockerCmdExecFactory withConnectionRequestTimeout(Integer connectionRequestTimeout) {
        clientFactory = clientFactory.connectionRequestTimeout(connectionRequestTimeout);
        return this;
    }

    public JerseyDockerCmdExecFactory withClientResponseFilters(ClientResponseFilter... clientResponseFilter) {
        clientFactory = clientFactory.clientResponseFilters(clientResponseFilter);
        return this;
    }

    public JerseyDockerCmdExecFactory withClientRequestFilters(ClientRequestFilter... clientRequestFilters) {
        clientFactory = clientFactory.clientRequestFilters(clientRequestFilters);
        return this;
    }

    public JerseyDockerCmdExecFactory withRequestEntityProcessing(RequestEntityProcessing requestEntityProcessing) {
        clientFactory = clientFactory.requestEntityProcessing(requestEntityProcessing);
        return this;
    }
}
