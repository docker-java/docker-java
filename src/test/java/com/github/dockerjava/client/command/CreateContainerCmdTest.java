package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


import java.lang.reflect.Method;
import java.util.Arrays;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.ContainerCreateResponse;
import com.github.dockerjava.client.model.ContainerInspectResponse;
import com.github.dockerjava.client.model.Volume;

public class CreateContainerCmdTest extends AbstractDockerClientTest {

	@BeforeTest
	public void beforeTest() throws DockerException {
		super.beforeTest();
	}
	
	@AfterTest
	public void afterTest() {
		super.afterTest();
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
	    super.beforeMethod(method);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		super.afterMethod(result);
	}
	
	@Test
	public void createContainerWithVolume() throws DockerException {
		
		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withVolumes(new Volume("/var/log")).withCmd("true").exec();

		tmpContainers.add(container.getId());
		
		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));
		
		ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
		
		LOG.info("Inspect container {}", containerInspectResponse.getConfig().getVolumes());
		
		assertThat(containerInspectResponse.getConfig().getVolumes().keySet(), contains("/var/log"));

		
	}
	
	@Test
	public void createContainerWithEnv() throws DockerException {
		
		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withEnv("VARIABLE=success").withCmd("env").exec();

		tmpContainers.add(container.getId());
		
		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));
		
		ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
		
		assertThat(Arrays.asList(containerInspectResponse.getConfig().getEnv()), contains("VARIABLE=success","HOME=/","PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"));

		dockerClient.startContainerCmd(container.getId()).exec();
		
		assertThat(logResponseStream(dockerClient.logContainerCmd(container.getId()).withStdOut().exec()), containsString("VARIABLE=success"));
	}
	
	@Test
	public void createContainerWithHostname() throws DockerException {
		
		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withHostName("docker-java").withCmd("env").exec();

		tmpContainers.add(container.getId());
		
		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));
		
		ContainerInspectResponse containerInspectResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
		
		assertThat(containerInspectResponse.getConfig().getHostName(), equalTo("docker-java"));

		dockerClient.startContainerCmd(container.getId()).exec();
		
		assertThat(logResponseStream(dockerClient.logContainerCmd(container.getId()).withStdOut().exec()), containsString("HOSTNAME=docker-java"));
	}
	

}
