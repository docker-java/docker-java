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
import java.util.Arrays;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.Bind;
import com.github.dockerjava.client.model.ContainerCreateResponse;
import com.github.dockerjava.client.model.ContainerInspectResponse;
import com.github.dockerjava.client.model.ExposedPort;
import com.github.dockerjava.client.model.Link;
import com.github.dockerjava.client.model.Ports;
import com.github.dockerjava.client.model.Volume;



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
		Volume volume1 = new Volume("/opt/webapp1");
		
		Volume volume2 = new Volume("/opt/webapp2");
		
		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox").withVolumes(volume1, volume2)
				.withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(containerInspectResponse.getConfig().getVolumes().keySet(),
				contains("/opt/webapp1", "/opt/webapp2"));

		
		dockerClient.startContainerCmd(container.getId()).withBinds(new Bind("/src/webapp1", volume1, true), new Bind("/src/webapp2", volume2)).exec();

		dockerClient.waitContainerCmd(container.getId()).exec();

		containerInspectResponse = dockerClient.inspectContainerCmd(container
				.getId()).exec();
		

		assertThat(Arrays.asList(containerInspectResponse.getVolumes()),
				contains(volume1, volume2));

		assertThat(Arrays.asList(containerInspectResponse.getVolumesRW()),
				contains(volume1, volume2));
		
		tmpContainers.add(container.getId());
	}
	
	@Test
	public void startContainerWithPortBindings() throws DockerException {
		
		ExposedPort tcp22 = ExposedPort.tcp(22);
		ExposedPort tcp23 = ExposedPort.tcp(23);
		
		ContainerCreateResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCmd("true").withExposedPorts(tcp22, tcp23).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		Ports portBindings = new Ports();
		portBindings.bind(tcp22, Ports.Binding(11022));
		portBindings.bind(tcp23, Ports.Binding(11023));

		dockerClient.startContainerCmd(container.getId()).withPortBindings(portBindings).exec();

		containerInspectResponse = dockerClient.inspectContainerCmd(container
				.getId()).exec();

		assertThat(Arrays.asList(containerInspectResponse.getConfig().getExposedPorts()),
				contains(tcp22, tcp23));

		assertThat(containerInspectResponse.getHostConfig().getPortBindings().getBindings().get(tcp22),
				is(equalTo(Ports.Binding("0.0.0.0", 11022))));
		
		assertThat(containerInspectResponse.getHostConfig().getPortBindings().getBindings().get(tcp23),
				is(equalTo(Ports.Binding("0.0.0.0", 11023))));
		
		tmpContainers.add(container.getId());
	}
	
	@Test
	public void startContainerWithLinking() throws DockerException {

		ContainerCreateResponse container1 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").withName("container1").exec();
		
		LOG.info("Created container1 {}", container1.toString());
		assertThat(container1.getId(), not(isEmptyString()));
		tmpContainers.add(container1.getId());

		dockerClient.startContainerCmd(container1.getId()).exec();

		ContainerInspectResponse containerInspectResponse1 = dockerClient
				.inspectContainerCmd(container1.getId()).exec();
		LOG.info("Container1 Inspect: {}", containerInspectResponse1.toString());

		assertThat(containerInspectResponse1.getConfig(), is(notNullValue()));
		assertThat(containerInspectResponse1.getId(), not(isEmptyString()));
		assertThat(containerInspectResponse1.getId(), startsWith(container1.getId()));
		assertThat(containerInspectResponse1.getName(), equalTo("/container1"));
		assertThat(containerInspectResponse1.getImageId(), not(isEmptyString()));
		assertThat(containerInspectResponse1.getState(), is(notNullValue()));
		assertThat(containerInspectResponse1.getState().isRunning(), is(true));

		if (!containerInspectResponse1.getState().isRunning()) {
			assertThat(containerInspectResponse1.getState().getExitCode(),
					is(equalTo(0)));
		}
		
		ContainerCreateResponse container2 = dockerClient
				.createContainerCmd("busybox").withCmd("true").withName("container2").exec();
		
		LOG.info("Created container2 {}", container2.toString());
		assertThat(container2.getId(), not(isEmptyString()));
		tmpContainers.add(container2.getId());

		dockerClient.startContainerCmd(container2.getId()).withLinks(new Link("container1", "container1Link")).exec();

		ContainerInspectResponse containerInspectResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();
		LOG.info("Container2 Inspect: {}", containerInspectResponse2.toString());
		
		assertThat(containerInspectResponse2.getConfig(), is(notNullValue()));
		assertThat(containerInspectResponse2.getId(), not(isEmptyString()));
		assertThat(containerInspectResponse2.getHostConfig(), is(notNullValue()));
		assertThat(containerInspectResponse2.getHostConfig().getLinks(), is(notNullValue()));
		assertThat(containerInspectResponse2.getHostConfig().getLinks(), equalTo(new String[] {"/container1:/container2/container1Link"}));
		assertThat(containerInspectResponse2.getId(), startsWith(container2.getId()));
		assertThat(containerInspectResponse2.getName(), equalTo("/container2"));
		assertThat(containerInspectResponse2.getImageId(), not(isEmptyString()));
		assertThat(containerInspectResponse2.getState(), is(notNullValue()));
		assertThat(containerInspectResponse2.getState().isRunning(), is(true));

	}

	
	@Test
	public void startContainer() throws DockerException {

		ContainerCreateResponse container = dockerClient
			.createContainerCmd("busybox").withCmd(new String[] { "top" }).exec();
		
		LOG.info("Created container {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		tmpContainers.add(container.getId());

		dockerClient.startContainerCmd(container.getId()).exec();

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();
		LOG.info("Container Inspect: {}", containerInspectResponse.toString());

		assertThat(containerInspectResponse.getConfig(), is(notNullValue()));
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
