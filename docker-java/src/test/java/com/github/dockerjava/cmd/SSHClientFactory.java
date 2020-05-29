package com.github.dockerjava.cmd;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DelegatingDockerCmdExecFactory;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DefaultDockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientConfigAware;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.jsch.SsshWithOKDockerHttpClient;
import com.jcraft.jsch.JSchException;

import java.io.IOException;

public class SSHClientFactory extends DelegatingDockerCmdExecFactory implements DockerClientConfigAware {

    private DefaultDockerCmdExecFactory dockerCmdExecFactory;
    private SsshWithOKDockerHttpClient httpClient;

    @Override
    public final DockerCmdExecFactory getDockerCmdExecFactory() {
        return dockerCmdExecFactory;
    }

    @Override
    public void init(DockerClientConfig dockerClientConfig) {

        final SsshWithOKDockerHttpClient.Factory factory = new SsshWithOKDockerHttpClient.Factory()
            .connectTimeout(30 * 1000)
            .dockerClientConfig(dockerClientConfig);

        final DefaultDockerClientConfig defaultDockerClientConfig = DefaultDockerClientConfig
            .createDefaultConfigBuilder()
            .withRegistryUrl(dockerClientConfig.getRegistryUrl())
            .build();

        if (!"ssh".equalsIgnoreCase(defaultDockerClientConfig.getDockerHost().getScheme())) {
            throw new RuntimeException("This FactoryType is supposed to test ssh connections.");
        }

        if (!dockerClientConfig.getDockerHost().equals(defaultDockerClientConfig.getDockerHost())) {
            // Docker Host was overwritten i.e. from com.github.dockerjava.cmd.swarm.SwarmCmdIT.initializeDockerClient
            // so we still use ssh connection and use inner binding to tcp
            if ("tcp".equalsIgnoreCase(dockerClientConfig.getDockerHost().getScheme())) {
                factory
                    .dockerClientConfig(defaultDockerClientConfig)
                    .useTcp(dockerClientConfig.getDockerHost().getPort());
            }
        }

        try {
            httpClient = factory.build();
            dockerCmdExecFactory = new DefaultDockerCmdExecFactory(httpClient, defaultDockerClientConfig.getObjectMapper());
        } catch (IOException | JSchException e) {
            throw new RuntimeException(e);
        }
        dockerCmdExecFactory.init(defaultDockerClientConfig);
    }

    public SSHClientFactory withDockerClientConfig(DockerClientConfig config) {
        init(config);
        return this;
    }

    public DockerClient build() {
        if (httpClient == null) {
            init(DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryUrl("https://index.docker.io/v1/")
                .build());
        }
        return DockerClientImpl.getInstance().withHttpClient(httpClient);
    }
}
