package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Test(groups = "integration")
public class PushImageCmdImplTest extends AbstractDockerClientTest {

	public static final Logger LOG = LoggerFactory
			.getLogger(PushImageCmdImplTest.class);

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
	public void pushLatest() throws Exception {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		LOG.info("Committing container: {}", container.toString());
		String imageId = dockerClient.commitCmd(container.getId()).withRepository(username + "/busybox").exec();

		// we have to block until image is pushed
		asString(dockerClient.pushImageCmd(username + "/busybox").exec());

        LOG.info("Removing image: {}", imageId);
		dockerClient.removeImageCmd(imageId).exec();
		
		String response = asString(dockerClient.pullImageCmd(username + "/busybox").exec());

		assertThat(response, not(containsString("HTTP code: 404")));
	}

	@Test
	public void pushExistentImage() throws Exception {

		assertThat(asString(dockerClient.pushImageCmd(username + "/xxx").exec()), containsString("error"));
	}

}

