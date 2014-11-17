package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class KillContainerCmdImplTest extends AbstractDockerClientTest {

	public static final Logger LOG = LoggerFactory
			.getLogger(KillContainerCmdImplTest.class);

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
	public void killContainer() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainerCmd(container.getId()).exec();

		LOG.info("Killing container: {}", container.getId());
		dockerClient.killContainerCmd(container.getId()).exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();
		LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

		assertThat(inspectContainerResponse.getState().isRunning(),
				is(equalTo(false)));
		assertThat(inspectContainerResponse.getState().getExitCode(),
				not(equalTo(0)));

	}
	
	@Test
	public void killNonExistingContainer() throws DockerException {

		try {
			dockerClient.killContainerCmd("non-existing").exec();
			fail("expected NotFoundException");
		} catch (NotFoundException e) {
		}
	}

}
