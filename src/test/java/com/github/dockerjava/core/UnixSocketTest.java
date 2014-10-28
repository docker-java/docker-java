package com.github.dockerjava.core;

import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerClient;

public class UnixSocketTest {

	@Test
	public void testUnixSocket() {
		DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder()
			    .withUri("unix://localhost/var/run/docker.sock")
			    .build();
			DockerClient docker = DockerClientBuilder.getInstance(config).build();
			docker.pingCmd();
	}
}
