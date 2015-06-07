package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.InputStream;
import java.lang.reflect.Method;

import com.github.dockerjava.core.util.DockerImageName;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class LogContainerCmdImplTest extends AbstractDockerClientTest {

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
	public void logContainer() throws Exception {

		String snippet = "hello world";

		CreateContainerResponse container = dockerClient
				.createContainerCmd(new DockerImageName("busybox")).withCmd("/bin/echo", snippet).exec();

		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId()).exec();
		
		int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();

		assertThat(exitCode, equalTo(0));

		InputStream response = dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec();

		String log = asString(response);
		
		//LOG.info("resonse: " + log);
		
		assertThat(log, endsWith(snippet));
	}
	
	@Test
	public void logNonExistingContainer() throws Exception {

		try {
			dockerClient.logContainerCmd("non-existing").withStdErr().withStdOut().exec();
			fail("expected NotFoundException");
		} catch (NotFoundException e) {
		}
	}
	
	@Test
	public void multipleLogContainer() throws Exception {

		String snippet = "hello world";

		CreateContainerResponse container = dockerClient
				.createContainerCmd(new DockerImageName("busybox")).withCmd("/bin/echo", snippet).exec();

		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId()).exec();
		
		int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();

		assertThat(exitCode, equalTo(0));

		InputStream response = dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec();

		response.close();
		
		//String log = asString(response);
		
		response = dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec();

		//log = asString(response);
		response.close();
		
		response = dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec();

		String log = asString(response);
		
		assertThat(log, endsWith(snippet));
	}


}
