package com.github.dockerjava.client.command;

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
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.ClientResponse;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;

import javax.ws.rs.core.Response;

public class BuildImageCmdTest extends AbstractDockerClientTest {

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
	public void testNginxDockerfileBuilder() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("nginx").getFile());

		Response response = dockerClient.buildImageCmd(baseDir).exec();

		StringWriter logwriter = new StringWriter();

        InputStream is = response.readEntity(InputStream.class);
		try {
            LineIterator itr = IOUtils.lineIterator(
                    is, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + "\n");
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(is);
		}

		String fullLog = logwriter.toString();
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(imageId).exec();
		assertThat(inspectImageResponse, not(nullValue()));
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());
		tmpImgs.add(inspectImageResponse.getId());

		assertThat(inspectImageResponse.getAuthor(),
				equalTo("Guillaume J. Charmes \"guillaume@dotcloud.com\""));
	}

	@Test
	public void testDockerBuilderAddUrl() throws DockerException, IOException {
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


	private String dockerfileBuild(File baseDir, String expectedText)
			throws DockerException, IOException {

		// Build image
		Response response = dockerClient.buildImageCmd(baseDir).exec();

		StringWriter logwriter = new StringWriter();

        InputStream is = response.readEntity(InputStream.class);
		try {
            LineIterator itr = IOUtils.lineIterator(
                    is, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + "\n");
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(is);
		}

		String fullLog = logwriter.toString();
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

		tmpContainers.add(container.getId());

		// Log container
		Response logResponse = logContainer(container
				.getId());

		assertThat(logResponseStream(logResponse), containsString(expectedText));

		return container.getId();
	}


	private Response logContainer(String containerId) {
		return dockerClient.logContainerCmd(containerId).withStdErr().withStdOut().exec();
	}

	@Test
	public void testNetCatDockerfileBuilder() throws DockerException,
			IOException, InterruptedException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("netcat").getFile());

		Response response = dockerClient.buildImageCmd(baseDir).withNoCache().exec();

		StringWriter logwriter = new StringWriter();

        InputStream is = response.readEntity(InputStream.class);
		try {
            LineIterator itr = IOUtils.lineIterator(
                    is, "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + "\n");
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(is);
		}

		String fullLog = logwriter.toString();
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(imageId).exec();
		assertThat(inspectImageResponse, not(nullValue()));
		assertThat(inspectImageResponse.getId(), not(nullValue()));
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());
		tmpImgs.add(inspectImageResponse.getId());

		CreateContainerResponse container = dockerClient.createContainerCmd(
				inspectImageResponse.getId()).exec();
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainerCmd(container.getId()).exec();
		tmpContainers.add(container.getId());

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
