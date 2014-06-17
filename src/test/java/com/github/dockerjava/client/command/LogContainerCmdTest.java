package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.ContainerCreateResponse;
import com.sun.jersey.api.client.ClientResponse;

public class LogContainerCmdTest extends AbstractDockerClientTest {

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
	public void attach() throws DockerException, IOException {

		String snippet = "hello world";

		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("/bin/echo", snippet).exec();
		
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId()).exec();
		tmpContainers.add(container.getId());

		int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();

		assertThat(exitCode, equalTo(0));

		ClientResponse response = dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec();

		assertThat(logResponseStream(response), endsWith(snippet));
	}


}
