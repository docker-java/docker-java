package com.github.dockerjava.core.command;

import static com.github.dockerjava.api.model.Capability.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Arrays;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Links;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class CreateContainerCmdImplTest extends AbstractDockerClientTest {

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
	public void createContainerWithExistingName() throws DockerException {

		String containerName = "generated_" + new SecureRandom().nextInt();

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("env")
				.withName(containerName).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		try {
			dockerClient.createContainerCmd("busybox").withCmd("env")
					.withName(containerName).exec();
			fail("expected ConflictException");
		} catch (ConflictException e) {
		}
	}

	@Test
	public void createContainerWithVolume() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withVolumes(new Volume("/var/log")).withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		LOG.info("Inspect container {}", inspectContainerResponse.getConfig()
				.getVolumes());

		assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(),
				contains("/var/log"));
	}

	@Test
	public void createContainerWithEnv() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withEnv("VARIABLE=success")
				.withCmd("env").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(
				Arrays.asList(inspectContainerResponse.getConfig().getEnv()),
				containsInAnyOrder("VARIABLE=success",
						"PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin"));

		dockerClient.startContainerCmd(container.getId()).exec();

		assertThat(asString(dockerClient.logContainerCmd(container.getId())
				.withStdOut().exec()), containsString("VARIABLE=success"));
	}

	@Test
	public void createContainerWithHostname() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withHostName("docker-java")
				.withCmd("env").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getConfig().getHostName(),
				equalTo("docker-java"));

		dockerClient.startContainerCmd(container.getId()).exec();

		assertThat(asString(dockerClient.logContainerCmd(container.getId())
				.withStdOut().exec()), containsString("HOSTNAME=docker-java"));
	}

	@Test
	public void createContainerWithName() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withName("container")
				.withCmd("env").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getName(), equalTo("/container"));

		try {
			dockerClient.createContainerCmd("busybox").withName("container")
					.withCmd("env").exec();
			fail("Expected ConflictException");
		} catch (ConflictException e) {
		}

	}
	
	@Test
	public void createContainerWithLink() throws DockerException {
	    
		CreateContainerResponse container1 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").withName("container1").exec();
		LOG.info("Created container1 {}", container1.toString());
		assertThat(container1.getId(), not(isEmptyString()));
		
		dockerClient.startContainerCmd(container1.getId()).exec();

		InspectContainerResponse inspectContainerResponse1 = dockerClient
				.inspectContainerCmd(container1.getId()).exec();
		LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());
		assertThat(inspectContainerResponse1.getState().isRunning(), is(true));

		HostConfig hostConfig = new HostConfig();
		hostConfig.setLinks(new Links(new Link("container1", "container1Link")));
		CreateContainerResponse container2 = dockerClient
				.createContainerCmd("busybox").withName("container2").withHostConfig(hostConfig)
				.withCmd("env").exec();
		LOG.info("Created container {}", container2.toString());
		assertThat(container2.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();
		assertThat(inspectContainerResponse2.getHostConfig().getLinks().getLinks(), equalTo(new Link[] {new Link("container1","container1Link")}));
	}

	@Test
	public void createContainerWithCapAddAndCapDrop() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCapAdd(NET_ADMIN)
				.withCapDrop(MKNOD).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getCapAdd()), contains(NET_ADMIN));

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getCapDrop()), contains(MKNOD));
	}

	@Test
	public void createContainerWithDns() throws DockerException {

		String aDnsServer = "8.8.8.8";
		String anotherDnsServer = "8.8.4.4";

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCmd("true").withDns(aDnsServer, anotherDnsServer).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()),
				contains(aDnsServer, anotherDnsServer));
	}

}
