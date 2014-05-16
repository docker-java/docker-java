package com.kpelykh.docker.client.test;


import static com.kpelykh.docker.client.DockerClient.asString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
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

import com.kpelykh.docker.client.DockerException;
import com.kpelykh.docker.client.model.CommitConfig;
import com.kpelykh.docker.client.model.ContainerConfig;
import com.kpelykh.docker.client.model.ContainerCreateResponse;

// delete here : https://index.docker.io/u/alexec/busybox/delete/
public class DockerPushTest extends AbstractDockerClientTest {
	
	public static final Logger LOG = LoggerFactory
			.getLogger(DockerPushTest.class);

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
	public void testPushLatest() throws Exception {
		setUpCredentials();
		
		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "true" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		tmpContainers.add(container.getId());
		
		LOG.info("Commiting container: {}", container.toString());
		CommitConfig commitConfig = new CommitConfig(container.getId());
		commitConfig.setRepo(getUsername() + "/busybox");
		
		String imageId = dockerClient
				.commit(commitConfig);
		
		logResponseStream(dockerClient.push(getUsername() + "/busybox"));
		
		dockerClient.removeImage(imageId);
		
		assertThat(asString(dockerClient.pull(getUsername() + "/busybox")), not(containsString("404")));
	}

	@Test
	public void testNotExistentImage() throws Exception {
		setUpCredentials();
		assertThat(logResponseStream(dockerClient.push(getUsername() + "/xxx")), containsString("error"));
	}

	private void setUpCredentials() {
		dockerClient.setCredentials(getUsername(), getPassword(), getEmail());
	}

	
}

