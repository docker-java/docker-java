package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.isEmptyString;
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

public class RemoveImageCmdTest extends AbstractDockerClientTest {

	public static final Logger LOG = LoggerFactory
			.getLogger(RemoveImageCmdTest.class);

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
	public void testRemoveImage() throws DockerException, InterruptedException {

		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("touch", "/test").exec();
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainerCmd(container.getId()).exec();
		tmpContainers.add(container.getId());

		LOG.info("Commiting container {}", container.toString());
		String imageId = dockerClient
				.commitCmd(container.getId()).exec();
		tmpImgs.add(imageId);

		dockerClient.stopContainerCmd(container.getId()).exec();
		dockerClient.killContainerCmd(container.getId()).exec();
		dockerClient.removeContainerCmd(container.getId()).exec();

		tmpContainers.remove(container.getId());
		LOG.info("Removing image: {}", imageId);
		dockerClient.removeImageCmd(imageId).exec();

		List containers = dockerClient.listContainersCmd().withShowAll(true).exec();
		Matcher matcher = not(hasItem(hasField("id", startsWith(imageId))));
		assertThat(containers, matcher);
	}


}

