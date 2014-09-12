package com.github.dockerjava.jaxrs;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;

public class DockerClientFactory {

	public static DockerClient getInstance() {
		return withDockerCmdExecFactory(DockerClientImpl.getInstance());
	}
	
	public static DockerClient getInstance(DockerClientConfig dockerClientConfig) {
		return withDockerCmdExecFactory(DockerClientImpl.getInstance(dockerClientConfig));
	}
	
	public static DockerClient getInstance(String serverUrl) {
		return withDockerCmdExecFactory(DockerClientImpl.getInstance(serverUrl));
	}
	
	private static DockerClient withDockerCmdExecFactory(DockerClientImpl dockerClient) {
		return dockerClient.withDockerCmdExecFactory(new DockerCmdExecFactoryImpl());
	}
}
