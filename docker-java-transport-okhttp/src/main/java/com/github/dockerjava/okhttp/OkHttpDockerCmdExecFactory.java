package com.github.dockerjava.okhttp;

import com.github.dockerjava.api.command.DelegatingDockerCmdExecFactory;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.DockerHttpClient;

/**
 * @deprecated use {@link OkDockerHttpClient} with {@link DockerClientImpl#withHttpClient(DockerHttpClient)}
 */
@Deprecated
public class OkHttpDockerCmdExecFactory extends DelegatingDockerCmdExecFactory implements DockerClientConfigAware {

    private DefaultDockerCmdExecFactory dockerCmdExecFactory;

    @Override
    public final DockerCmdExecFactory getDockerCmdExecFactory() {
        return dockerCmdExecFactory;
    }

    @Override
    public void init(DockerClientConfig dockerClientConfig) {
        dockerCmdExecFactory = new DefaultDockerCmdExecFactory(
            new OkDockerHttpClient(dockerClientConfig),
            dockerClientConfig.getObjectMapper()
        );
        dockerCmdExecFactory.init(dockerClientConfig);
    }
}
