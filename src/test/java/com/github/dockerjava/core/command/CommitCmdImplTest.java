package com.github.dockerjava.core.command;

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

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class CommitCmdImplTest extends AbstractDockerClientTest {

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

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("touch", "/test").exec();
		
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainerCmd(container.getId()).exec();

		LOG.info("Commiting container: {}", container.toString());
		String imageId = dockerClient
				.commitCmd(container.getId()).exec();

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(imageId).exec();
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());

		assertThat(inspectImageResponse,
				hasField("container", startsWith(container.getId())));
		assertThat(inspectImageResponse.getContainerConfig().getImage(),
				equalTo("busybox"));

		InspectImageResponse busyboxImg = dockerClient.inspectImageCmd("busybox").exec();

		assertThat(inspectImageResponse.getParent(),
				equalTo(busyboxImg.getId()));
	}
	
	
	@Test
	public void commitNonExistingContainer() throws DockerException {
		try {
			dockerClient.commitCmd("non-existent").exec();
			fail("expected NotFoundException");
		} catch (NotFoundException e) {
		}
	}

}
