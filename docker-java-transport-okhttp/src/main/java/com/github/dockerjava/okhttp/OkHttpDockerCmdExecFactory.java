package com.github.dockerjava.okhttp;

import com.github.dockerjava.api.command.DelegatingDockerCmdExecFactory;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;

/**
 * @deprecated use {@link OkDockerHttpClient} with {@link DockerClientImpl#withHttpClient(DockerHttpClient)}
 */
@Deprecated
public class OkHttpDockerCmdExecFactory extends DelegatingDockerCmdExecFactory implements DockerClientConfigAware {

    private OkDockerHttpClient.Factory clientFactory = new OkDockerHttpClient.Factory();

    @Deprecated
    protected Integer connectTimeout;

    @Deprecated
    protected Integer readTimeout;

    private DefaultDockerCmdExecFactory dockerCmdExecFactory;

    /**
     * Configure connection timeout in milliseconds
     */
    public OkHttpDockerCmdExecFactory withConnectTimeout(Integer connectTimeout) {
        clientFactory = clientFactory.connectTimeout(connectTimeout);
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * Configure read timeout in milliseconds
     */
    public OkHttpDockerCmdExecFactory withReadTimeout(Integer readTimeout) {
        clientFactory = clientFactory.readTimeout(readTimeout);
        this.readTimeout = readTimeout;
        return this;
    }

    public OkHttpDockerCmdExecFactory setRetryOnConnectionFailure(Boolean retryOnConnectionFailure) {
        this.clientFactory = clientFactory.retryOnConnectionFailure(retryOnConnectionFailure);
        return this;
    }

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
}
