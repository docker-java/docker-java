package com.github.dockerjava.core;

import java.util.Iterator;
import java.util.ServiceLoader;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig.DockerClientConfigBuilder;

public class DockerClientBuilder {

    private static Class<? extends DockerCmdExecFactory> factoryClass;

    private static ServiceLoader<DockerCmdExecFactory> serviceLoader = ServiceLoader.load(DockerCmdExecFactory.class);

    static {
        reloadFactory();
    }

    private static void reloadFactory() {
        serviceLoader.reload();
        Iterator<DockerCmdExecFactory> iterator = serviceLoader.iterator();
        if (iterator.hasNext()) {
            factoryClass = iterator.next().getClass();
        }
    }

    private DockerClientImpl dockerClient = null;

    private DockerCmdExecFactory dockerCmdExecFactory = null;

    private DockerClientBuilder(DockerClientImpl dockerClient) {
        this.dockerClient = dockerClient;
    }

    public static DockerClientBuilder getInstance() {
        return new DockerClientBuilder(DockerClientImpl.getInstance());
    }

    public static DockerClientBuilder getInstance(DockerClientConfigBuilder dockerClientConfigBuilder) {
        return getInstance(dockerClientConfigBuilder.build());
    }

    public static DockerClientBuilder getInstance(DockerClientConfig dockerClientConfig) {
        return new DockerClientBuilder(DockerClientImpl.getInstance(dockerClientConfig));
    }

    public static DockerClientBuilder getInstance(String serverUrl) {
        return new DockerClientBuilder(DockerClientImpl.getInstance(serverUrl));
    }

    public static DockerCmdExecFactory getDefaultDockerCmdExecFactory() {
        if (factoryClass == null) {
            throw new RuntimeException("Fatal: Can't find any implementation of '"
                    + DockerCmdExecFactory.class.getName() + "' in the current classpath.");
        }

        try {
            return factoryClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Fatal: Can't create new instance of '" + factoryClass.getName() + "'");
        }
    }

    public DockerClientBuilder withDockerCmdExecFactory(DockerCmdExecFactory dockerCmdExecFactory) {
        this.dockerCmdExecFactory = dockerCmdExecFactory;
        return this;
    }

    public DockerClientBuilder withServiceLoaderClassLoader(ClassLoader classLoader) {
        serviceLoader = ServiceLoader.load(DockerCmdExecFactory.class, classLoader);
        reloadFactory();
        return this;
    }

    public DockerClient build() {
        if (dockerCmdExecFactory != null) {
            dockerClient.withDockerCmdExecFactory(dockerCmdExecFactory);
        } else {
            dockerClient.withDockerCmdExecFactory(getDefaultDockerCmdExecFactory());
        }

        return dockerClient;
    }
}
