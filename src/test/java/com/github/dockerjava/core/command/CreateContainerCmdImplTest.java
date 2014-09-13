package com.github.dockerjava.core.command;

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

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.client.AbstractDockerClientTest;

public class CreateContainerCmdImplTest extends AbstractDockerClientTest {

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

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withVolumes(new Volume("/var/log")).withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

		LOG.info("Inspect container {}", inspectContainerResponse.getConfig().getVolumes());

		assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), contains("/var/log"));
	}

	@Test
	public void createContainerWithEnv() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withEnv("VARIABLE=success").withCmd("env").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), contains("VARIABLE=success","HOME=/","PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"));

		dockerClient.startContainerCmd(container.getId()).exec();

		assertThat(asString(dockerClient.logContainerCmd(container.getId()).withStdOut().exec()), containsString("VARIABLE=success"));
	}

	@Test
	public void createContainerWithHostname() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withHostName("docker-java").withCmd("env").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getConfig().getHostName(), equalTo("docker-java"));

		dockerClient.startContainerCmd(container.getId()).exec();

		assertThat(asString(dockerClient.logContainerCmd(container.getId()).withStdOut().exec()), containsString("HOSTNAME=docker-java"));
	}
	
	@Test
	public void createContainerWithName() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withName("container").withCmd("env").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getName(), equalTo("/container"));
		
		
		try {
			dockerClient.createContainerCmd("busybox").withName("container").withCmd("env").exec();
			fail("Expected ConflictException");
		} catch (ConflictException e) {
		}

	}


}
