package com.github.dockerjava.jaxrs2;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;

public class DockerClientBuilder {

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
		return dockerClient
				.withDockerCmdExecFactory(new DockerCmdExecFactoryImpl());
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
