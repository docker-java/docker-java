package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

public class BuildImageCmdImplTest extends AbstractDockerClientTest {

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
	public void testNginxDockerfileBuilder() {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("nginx").getFile());

		InputStream response = dockerClient.buildImageCmd(baseDir).withNoCache().exec();

		String fullLog = asString(response);
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(imageId).exec();
		assertThat(inspectImageResponse, not(nullValue()));
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());
		
		assertThat(inspectImageResponse.getAuthor(),
				equalTo("Guillaume J. Charmes \"guillaume@dotcloud.com\""));
	}

	@Test
	public void testDockerBuilderAddUrl()  {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddUrl").getFile());
		dockerfileBuild(baseDir, "Docker");
	}

	@Test
	public void testDockerBuilderAddFileInSubfolder() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddFileInSubfolder").getFile());
		dockerfileBuild(baseDir, "Successfully executed testrun.sh");
	}

	@Test
	public void testDockerBuilderAddFolder() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddFolder").getFile());
		dockerfileBuild(baseDir, "Successfully executed testAddFolder.sh");
	}


	private String dockerfileBuild(File baseDir, String expectedText) {

		// Build image
		InputStream response = dockerClient.buildImageCmd(baseDir).withNoCache().exec();

		String fullLog = asString(response);
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		// Create container based on image
		CreateContainerResponse container = dockerClient.createContainerCmd(
				imageId).exec();

		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId()).exec();
		dockerClient.waitContainerCmd(container.getId()).exec();

		// Log container
		InputStream logResponse = logContainer(container
				.getId());

		assertThat(asString(logResponse), containsString(expectedText));

		return container.getId();
	}


	private InputStream logContainer(String containerId) {
		return dockerClient.logContainerCmd(containerId).withStdErr().withStdOut().exec();
	}

	@Test
	public void testNetCatDockerfileBuilder() throws InterruptedException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("netcat").getFile());

		InputStream response = dockerClient.buildImageCmd(baseDir).withNoCache().exec();

		String fullLog = asString(response);
		
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(imageId).exec();
		assertThat(inspectImageResponse, not(nullValue()));
		assertThat(inspectImageResponse.getId(), not(nullValue()));
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());
		
		CreateContainerResponse container = dockerClient.createContainerCmd(
				inspectImageResponse.getId()).exec();
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainerCmd(container.getId()).exec();
		
		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getId(), notNullValue());
		assertThat(inspectContainerResponse.getNetworkSettings().getPorts(),
				notNullValue());

		// No use as such if not running on the server
//		for (Ports.Port p : inspectContainerResponse.getNetworkSettings().getPorts().getAllPorts()) {
//			int port = Integer.valueOf(p.getHostPort());
//			LOG.info("Checking port {} is open", port);
//			assertThat(available(port), is(false));
//		}
		dockerClient.stopContainerCmd(container.getId()).withTimeout(0).exec();

	}

	@Test
	public void testAddAndCopySubstitution () throws DockerException, IOException {
			File baseDir = new File(Thread.currentThread().getContextClassLoader()
					.getResource("testENVSubstitution").getFile());
			dockerfileBuild(baseDir, "testENVSubstitution successfully completed");

	}
}
