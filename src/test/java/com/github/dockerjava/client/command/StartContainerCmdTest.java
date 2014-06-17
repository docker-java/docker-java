package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import java.lang.reflect.Method;
import java.util.HashMap;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.*;



public class StartContainerCmdTest extends AbstractDockerClientTest {

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
	public void startContainerWithVolumes() throws DockerException {

		// see http://docs.docker.io/use/working_with_volumes/
		
		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withVolumes("/logs_from_host")
				.withCmd(new String[] { "true" }).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(containerInspectResponse.getConfig().getVolumes().keySet(),
				contains("/logs_from_host"));

		dockerClient.startContainerCmd(container.getId()).withBinds("/var/log:/logs_from_host:ro").exec();

		dockerClient.waitContainerCmd(container.getId()).exec();

		containerInspectResponse = dockerClient.inspectContainerCmd(container
				.getId()).exec();
		
		assertThat(containerInspectResponse.getVolumes().get("/logs_from_host"),
				equalTo("/var/log"));

		assertThat(containerInspectResponse.getVolumesRW().get("/logs_from_host"),
				equalTo(false));
		
		tmpContainers.add(container.getId());
	}
	
	@Test
	public void startContainerWithPortBindings() throws DockerException {
		
		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCmd("true").withExposedPorts("22/tcp").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		Ports portBindings = new Ports();
		
		portBindings.addPort("22/tcp", "", "11022");

		dockerClient.startContainerCmd(container.getId()).withPortBindings(portBindings).exec();

		containerInspectResponse = dockerClient.inspectContainerCmd(container
				.getId()).exec();
		
		assertThat(containerInspectResponse.getConfig().getExposedPorts().keySet(),
				contains("22/tcp"));

		assertThat(containerInspectResponse.getHostConfig().getPortBindings().getAllPorts(),
				contains(new Ports.Port("tcp", "22", "0.0.0.0", "11022")));
		
		tmpContainers.add(container.getId());
	}
	
	@Test
	public void startContainer() throws DockerException {

		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withCmd(new String[] { "true" }).exec();
		
		LOG.info("Created container {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		tmpContainers.add(container.getId());

		dockerClient.startContainerCmd(container.getId()).exec();

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();
		LOG.info("Container Inspect: {}", containerInspectResponse.toString());

		assertThat(containerInspectResponse.config, is(notNullValue()));
		assertThat(containerInspectResponse.getId(), not(isEmptyString()));

		assertThat(containerInspectResponse.getId(),
				startsWith(container.getId()));

		assertThat(containerInspectResponse.getImageId(), not(isEmptyString()));
		assertThat(containerInspectResponse.getState(), is(notNullValue()));

		assertThat(containerInspectResponse.getState().isRunning(), is(true));

		if (!containerInspectResponse.getState().isRunning()) {
			assertThat(containerInspectResponse.getState().getExitCode(),
					is(equalTo(0)));
		}

	}


}
