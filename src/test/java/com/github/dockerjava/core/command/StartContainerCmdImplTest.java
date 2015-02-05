package com.github.dockerjava.core.command;

import static com.github.dockerjava.api.model.AccessMode.ro;
import static com.github.dockerjava.api.model.Capability.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import java.lang.reflect.Method;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.model.*;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class StartContainerCmdImplTest extends AbstractDockerClientTest {

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

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withVolumes(volume1, volume2)
				.withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(),
				contains("/opt/webapp1", "/opt/webapp2"));

		dockerClient
				.startContainerCmd(container.getId())
				.withBinds(new Bind("/src/webapp1", volume1, ro),
						new Bind("/src/webapp2", volume2)).exec();

		dockerClient.waitContainerCmd(container.getId()).exec();

		inspectContainerResponse = dockerClient.inspectContainerCmd(
				container.getId()).exec();

		assertContainerHasVolumes(inspectContainerResponse, volume1, volume2);

		assertThat(Arrays.asList(inspectContainerResponse.getVolumesRW()),
				contains(volume1, volume2));

	}

	@Test
	public void startContainerWithVolumesFrom() throws DockerException {

		Volume volume1 = new Volume("/opt/webapp1");
		Volume volume2 = new Volume("/opt/webapp2");

		String container1Name = UUID.randomUUID().toString();

		CreateContainerResponse container1 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999")
				.withName(container1Name).exec();
		LOG.info("Created container1 {}", container1.toString());

		dockerClient
				.startContainerCmd(container1.getId())
				.withBinds(new Bind("/src/webapp1", volume1),
						new Bind("/src/webapp2", volume2)).exec();
		LOG.info("Started container1 {}", container1.toString());

		InspectContainerResponse inspectContainerResponse1 = dockerClient
				.inspectContainerCmd(container1.getId()).exec();

		assertContainerHasVolumes(inspectContainerResponse1, volume1, volume2);

		CreateContainerResponse container2 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
		LOG.info("Created container2 {}", container2.toString());

		dockerClient.startContainerCmd(container2.getId())
				.withVolumesFrom(container1Name).exec();
		LOG.info("Started container2 {}", container2.toString());

		InspectContainerResponse inspectContainerResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();

		assertContainerHasVolumes(inspectContainerResponse2, volume1, volume2);
	}

	@Test
	public void startContainerWithDns() throws DockerException {

		String aDnsServer = "8.8.8.8";
		String anotherDnsServer = "8.8.4.4";

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId())
				.withDns(aDnsServer, anotherDnsServer).exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getDns()), contains(aDnsServer, anotherDnsServer));
	}

	@Test
	public void startContainerWithDnsSearch() throws DockerException {

		String dnsSearch = "example.com";

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		dockerClient.startContainerCmd(container.getId())
				.withDnsSearch(dnsSearch).exec();

		inspectContainerResponse = dockerClient.inspectContainerCmd(
				container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getDnsSearch()), contains(dnsSearch));
	}

	@Test
	public void startContainerWithPortBindings() throws DockerException {

		ExposedPort tcp22 = ExposedPort.tcp(22);
		ExposedPort tcp23 = ExposedPort.tcp(23);

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true")
				.withExposedPorts(tcp22, tcp23).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		Ports portBindings = new Ports();
		portBindings.bind(tcp22, Ports.Binding(11022));
		portBindings.bind(tcp23, Ports.Binding(11023));
		portBindings.bind(tcp23, Ports.Binding(11024));

		dockerClient.startContainerCmd(container.getId())
				.withPortBindings(portBindings).exec();

		inspectContainerResponse = dockerClient.inspectContainerCmd(
				container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getConfig()
				.getExposedPorts()), contains(tcp22, tcp23));

		assertThat(inspectContainerResponse.getHostConfig().getPortBindings()
				.getBindings().get(tcp22)[0], is(equalTo(Ports.Binding(11022))));

		assertThat(inspectContainerResponse.getHostConfig().getPortBindings()
				.getBindings().get(tcp23)[0], is(equalTo(Ports.Binding(11023))));

		assertThat(inspectContainerResponse.getHostConfig().getPortBindings()
				.getBindings().get(tcp23)[1], is(equalTo(Ports.Binding(11024))));

	}

	@Test
	public void startContainerWithConflictingPortBindings()
			throws DockerException {

		ExposedPort tcp22 = ExposedPort.tcp(22);
		ExposedPort tcp23 = ExposedPort.tcp(23);

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true")
				.withExposedPorts(tcp22, tcp23).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		Ports portBindings = new Ports();
		portBindings.bind(tcp22, Ports.Binding(11022));
		portBindings.bind(tcp23, Ports.Binding(11022));

		try {
			dockerClient.startContainerCmd(container.getId())
					.withPortBindings(portBindings).exec();
			fail("expected InternalServerErrorException");
		} catch (InternalServerErrorException e) {

		}

	}

	@Test
	public void startContainerWithLinking() throws DockerException {

		CreateContainerResponse container1 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999")
				.withName("container1").exec();

		LOG.info("Created container1 {}", container1.toString());
		assertThat(container1.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container1.getId()).exec();

		InspectContainerResponse inspectContainerResponse1 = dockerClient
				.inspectContainerCmd(container1.getId()).exec();
		LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

		assertThat(inspectContainerResponse1.getConfig(), is(notNullValue()));
		assertThat(inspectContainerResponse1.getId(), not(isEmptyString()));
		assertThat(inspectContainerResponse1.getId(),
				startsWith(container1.getId()));
		assertThat(inspectContainerResponse1.getName(), equalTo("/container1"));
		assertThat(inspectContainerResponse1.getImageId(), not(isEmptyString()));
		assertThat(inspectContainerResponse1.getState(), is(notNullValue()));
		assertThat(inspectContainerResponse1.getState().isRunning(), is(true));

		if (!inspectContainerResponse1.getState().isRunning()) {
			assertThat(inspectContainerResponse1.getState().getExitCode(),
					is(equalTo(0)));
		}

		CreateContainerResponse container2 = dockerClient
				.createContainerCmd("busybox").withCmd("true")
				.withName("container2").exec();

		LOG.info("Created container2 {}", container2.toString());
		assertThat(container2.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container2.getId())
				.withLinks(new Link("container1", "container1Link")).exec();

		InspectContainerResponse inspectContainerResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();
		LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

		assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
		assertThat(inspectContainerResponse2.getId(), not(isEmptyString()));
		assertThat(inspectContainerResponse2.getHostConfig(),
				is(notNullValue()));
		assertThat(inspectContainerResponse2.getHostConfig().getLinks(),
				is(notNullValue()));
		assertThat(inspectContainerResponse2.getHostConfig().getLinks()
				.getLinks(), equalTo(new Link[] { new Link("container1",
				"container1Link") }));
		assertThat(inspectContainerResponse2.getId(),
				startsWith(container2.getId()));
		assertThat(inspectContainerResponse2.getName(), equalTo("/container2"));
		assertThat(inspectContainerResponse2.getImageId(), not(isEmptyString()));
		assertThat(inspectContainerResponse2.getState(), is(notNullValue()));
		assertThat(inspectContainerResponse2.getState().isRunning(), is(true));

	}

	@Test
	public void startContainer() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd(new String[] { "top" })
				.exec();

		LOG.info("Created container {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId()).exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();
		LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

		assertThat(inspectContainerResponse.getConfig(), is(notNullValue()));
		assertThat(inspectContainerResponse.getId(), not(isEmptyString()));

		assertThat(inspectContainerResponse.getId(),
				startsWith(container.getId()));

		assertThat(inspectContainerResponse.getImageId(), not(isEmptyString()));
		assertThat(inspectContainerResponse.getState(), is(notNullValue()));

		assertThat(inspectContainerResponse.getState().isRunning(), is(true));

		if (!inspectContainerResponse.getState().isRunning()) {
			assertThat(inspectContainerResponse.getState().getExitCode(),
					is(equalTo(0)));
		}
	}

	@Test
	public void testStartNonExistingContainer() throws DockerException {
		try {
			dockerClient.startContainerCmd("non-existing").exec();
			fail("expected NotFoundException");
		} catch (NotFoundException e) {
		}
	}

	/**
	 * This tests support for --net option for the docker run command:
	 * --net="bridge" Set the Network mode for the container 'bridge': creates a
	 * new network stack for the container on the docker bridge 'none': no
	 * networking for this container 'container:': reuses another container
	 * network stack 'host': use the host network stack inside the container.
	 * Note: the host mode gives the container full access to local system
	 * services such as D-bus and is therefore considered insecure.
	 */
	@Test
	public void startContainerWithNetworkMode() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		dockerClient.startContainerCmd(container.getId())
				.withNetworkMode("host").exec();

		inspectContainerResponse = dockerClient.inspectContainerCmd(
				container.getId()).exec();

		assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(),
				is(equalTo("host")));
	}

	@Test
	public void startContainerWithCapAddAndCapDrop() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId()).withCapAdd(NET_ADMIN)
				.withCapDrop(MKNOD).exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getState().isRunning(), is(true));

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getCapAdd()), contains(NET_ADMIN));

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getCapDrop()), contains(MKNOD));
	}

	@Test
	public void startContainerWithDevices() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainerCmd(container.getId())
				.withDevices(new Device("rwm", "/dev/nulo", "/dev/zero"))
				.exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getState().isRunning(), is(true));

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getDevices()), contains(new Device("rwm", "/dev/nulo",
				"/dev/zero")));
	}

	@Test
	public void startContainerWithRestartPolicy() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);

		dockerClient.startContainerCmd(container.getId())
				.withRestartPolicy(restartPolicy).exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getState().isRunning(), is(true));

		assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(),
				is(equalTo(restartPolicy)));
	}

	@Test
	public void existingHostConfigIsPreservedByBlankStartCmd()
			throws DockerException {

		String dnsServer = "8.8.8.8";

		// prepare a container with custom DNS
		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withDns(dnsServer)
				.withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		// start container _without_any_customization_ (important!)
		dockerClient.startContainerCmd(container.getId()).exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		// The DNS setting survived.
		assertThat(inspectContainerResponse.getHostConfig().getDns(),
				is(notNullValue()));
		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getDns()), contains(dnsServer));
	}

	@Test
	public void existingHostConfigIsResetByConfiguredStartCmd()
			throws DockerException {
		// As of version 1.3.2, Docker assumes that you either configure a
		// container
		// when creating it or when starting it, but not mixing both.
		// See https://github.com/docker-java/docker-java/pull/111
		// If this test starts to fail, this behavior changed and a review of
		// implementation
		// and documentation might be needed.

		String dnsServer = "8.8.8.8";

		// prepare a container with custom DNS
		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withDns(dnsServer)
				.withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		// modify another setting in start command. Leave DNS unchanged.
		dockerClient.startContainerCmd(container.getId())
				.withPublishAllPorts(true).exec();

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		// although start did not modify DNS Settings, they were reset to their
		// default.
		assertThat(inspectContainerResponse.getHostConfig().getDns(),
				is(nullValue(String[].class)));
	}

	@Test
	public void anUnconfiguredCommandSerializesToEmptyJson() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		StartContainerCmd command = dockerClient.startContainerCmd("");
		assertThat(objectMapper.writeValueAsString(command), is("{}"));
	}
}
