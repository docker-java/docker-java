package com.github.dockerjava.core;

import java.util.ServiceLoader;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;

public class DockerClientBuilder {
	
	private static ServiceLoader<DockerCmdExecFactory> serviceLoader = ServiceLoader.load(DockerCmdExecFactory.class);

	private DockerClientImpl dockerClient = null;

	private DockerClientBuilder(DockerClientImpl dockerClient) {
		this.dockerClient = dockerClient;
	}

	public static DockerClientBuilder getInstance() {
		return new DockerClientBuilder(withDefaultDockerCmdExecFactory(DockerClientImpl.getInstance()));
	}

	public static DockerClientBuilder getInstance(DockerClientConfig dockerClientConfig) {
		return new DockerClientBuilder(withDefaultDockerCmdExecFactory(DockerClientImpl
				.getInstance(dockerClientConfig)));
	}

	public static DockerClientBuilder getInstance(String serverUrl) {
		return new DockerClientBuilder(withDefaultDockerCmdExecFactory(DockerClientImpl
				.getInstance(serverUrl)));
	}

	private static DockerClientImpl withDefaultDockerCmdExecFactory(
			DockerClientImpl dockerClient) {
		
		DockerCmdExecFactory dockerCmdExecFactory = getDefaultDockerCmdExecFactory();
		
		return dockerClient
				.withDockerCmdExecFactory(dockerCmdExecFactory);
	}

	public static DockerCmdExecFactory getDefaultDockerCmdExecFactory() {
		if(!serviceLoader.iterator().hasNext()) {
			throw new RuntimeException("Fatal: Can't find any implementation of '" + DockerCmdExecFactory.class.getName() +  "' in the current classpath.");
		}
		
		return serviceLoader.iterator().next();
	}

	public DockerClientBuilder withDockerCmdExecFactory(
			DockerCmdExecFactory dockerCmdExecFactory) {
		dockerClient = dockerClient
				.withDockerCmdExecFactory(dockerCmdExecFactory);
		return this;
	}

	public DockerClient build() {
		return dockerClient;
	}
}
