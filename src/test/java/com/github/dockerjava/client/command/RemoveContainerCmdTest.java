package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

import java.lang.reflect.Method;
import java.util.List;

import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.ContainerCreateResponse;

public class RemoveContainerCmdTest extends AbstractDockerClientTest {
	
	public static final Logger LOG = LoggerFactory
			.getLogger(RemoveContainerCmdTest.class);

    String username;

	@BeforeTest
	public void beforeTest() throws DockerException {
		super.beforeTest();
        username = dockerClient.authConfig().getUsername();
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
	public void removeContainer() throws DockerException {

		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true").exec();

		dockerClient.startContainerCmd(container.getId()).exec();
		dockerClient.waitContainerCmd(container.getId()).exec();
		tmpContainers.add(container.getId());

		LOG.info("Removing container: {}", container.getId());
		dockerClient.removeContainerCmd(container.getId()).exec();

		List containers2 = dockerClient.listContainersCmd().withShowAll(true).exec();
		Matcher matcher = not(hasItem(hasField("id",
				startsWith(container.getId()))));
		assertThat(containers2, matcher);

	}

	
}

