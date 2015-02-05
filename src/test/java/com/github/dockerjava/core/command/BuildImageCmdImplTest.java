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

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.EventStreamItem;
import com.github.dockerjava.client.AbstractDockerClientTest;


@Test(groups = "integration")
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
		String response = dockerfileBuild(baseDir);
		assertThat(response, containsString("Docker"));
	}

	@Test
	public void testDockerBuilderAddFileInSubfolder() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddFileInSubfolder").getFile());
		String response = dockerfileBuild(baseDir);
		assertThat(response, containsString("Successfully executed testrun.sh"));
	}
	
	@Test
	public void testDockerBuilderAddFilesViaWildcard() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddFilesViaWildcard").getFile());
		String response = dockerfileBuild(baseDir);
		assertThat(response, containsString("Successfully executed testinclude1.sh"));
		assertThat(response, not(containsString("Successfully executed testinclude2.sh")));
	}

	@Test
	public void testDockerBuilderAddFolder() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddFolder").getFile());
		String response = dockerfileBuild(baseDir);
		assertThat(response, containsString("Successfully executed testAddFolder.sh"));
	}


	private String dockerfileBuild(File baseDir) {

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

		//assertThat(asString(logResponse), containsString(expectedText));

		return asString(logResponse);
	}


	private InputStream logContainer(String containerId) {
		return dockerClient.logContainerCmd(containerId).withStdErr().withStdOut().exec();
	}

	@Test(expectedExceptions={DockerClientException.class})
	public void testDockerfileIgnored() {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testDockerfileIgnored").getFile());
		dockerClient.buildImageCmd(baseDir).withNoCache().exec();
	}

	@Test(expectedExceptions={DockerClientException.class})
		public void testInvalidDockerIgnorePattern() {
			File baseDir = new File(Thread.currentThread().getContextClassLoader()
					.getResource("testInvalidDockerignorePattern").getFile());
			dockerClient.buildImageCmd(baseDir).withNoCache().exec();
		}

	@Test
	public void testDockerIgnore() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testDockerignore").getFile());
		String response = dockerfileBuild(baseDir);
		assertThat(response, containsString("/tmp/a/a /tmp/a/c /tmp/a/d"));
	}

	@Test
	public void testNetCatDockerfileBuilder() throws InterruptedException, IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("netcat").getFile());

		Iterable<EventStreamItem> response = dockerClient.buildImageCmd(baseDir).withNoCache().exec().getItems();

                String imageId = null;

		for(EventStreamItem item : response) {
                  String text = item.getStream();
                  if( text.startsWith("Successfully built ")) {
                    imageId = StringUtils.substringBetween(text,
				"Successfully built ", "\n").trim();
                  }
                }
		
		assertNotNull(imageId, "Not successful in build");


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
			String response = dockerfileBuild(baseDir);
			assertThat(response, containsString("testENVSubstitution successfully completed"));
	}
}
