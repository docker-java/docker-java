package com.github.dockerjava.core;

import java.util.ServiceLoader;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DockerClientConfig.DockerClientConfigBuilder;

public class DockerClientBuilder {
	
	private static ServiceLoader<DockerCmdExecFactory> serviceLoader = ServiceLoader.load(DockerCmdExecFactory.class);

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
		return new DockerClientBuilder(DockerClientImpl
				.getInstance(dockerClientConfig));
	}

	public static DockerClientBuilder getInstance(String serverUrl) {
		return new DockerClientBuilder(DockerClientImpl
				.getInstance(serverUrl));
	}

	public static DockerCmdExecFactory getDefaultDockerCmdExecFactory() {
		if(!serviceLoader.iterator().hasNext()) {
			throw new RuntimeException("Fatal: Can't find any implementation of '" + DockerCmdExecFactory.class.getName() +  "' in the current classpath.");
		}
		
		return serviceLoader.iterator().next();
	}

	public DockerClientBuilder withDockerCmdExecFactory(
			DockerCmdExecFactory dockerCmdExecFactory) {
		this.dockerCmdExecFactory = dockerCmdExecFactory;
		return this;
	}
		
	public DockerClientBuilder withServiceLoaderClassPath(ClassPath classPath)
	{
		 serviceLoader = ServiceLoader.load(DockerCmdExecFactory.class, classPath);
		 return this;
	}

	public DockerClient build() {
	        if(dockerCmdExecFactory != null) {
	            dockerClient.withDockerCmdExecFactory(dockerCmdExecFactory);
	        }
	        else {
	            dockerClient.withDockerCmdExecFactory(getDefaultDockerCmdExecFactory());
	        }
	        
		return dockerClient;
	}
}
