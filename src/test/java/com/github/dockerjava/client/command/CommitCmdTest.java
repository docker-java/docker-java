package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

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
import com.github.dockerjava.client.model.ImageInspectResponse;

public class CommitCmdTest extends AbstractDockerClientTest {

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
	public void commit() throws DockerException {

		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withCmd(new String[] { "touch", "/test" }).exec();
		
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainerCmd(container.getId()).exec();
		tmpContainers.add(container.getId());

		LOG.info("Commiting container: {}", container.toString());
		String imageId = dockerClient
				.commitCmd(container.getId()).exec();
		tmpImgs.add(imageId);

		ImageInspectResponse imageInspectResponse = dockerClient
				.inspectImageCmd(imageId).exec();
		LOG.info("Image Inspect: {}", imageInspectResponse.toString());

		assertThat(imageInspectResponse,
				hasField("container", startsWith(container.getId())));
		assertThat(imageInspectResponse.getContainerConfig().getImage(),
				equalTo("busybox"));

		ImageInspectResponse busyboxImg = dockerClient.inspectImageCmd("busybox").exec();

		assertThat(imageInspectResponse.getParent(),
				equalTo(busyboxImg.getId()));
	}

}
