package com.github.dockerjava.core.command;

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.client.AbstractDockerClientTest;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.UUID;

import static com.github.dockerjava.api.model.Capability.MKNOD;
import static com.github.dockerjava.api.model.Capability.NET_ADMIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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

		Volume volume = new Volume("/var/log");
		
		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withVolumes(volume).withCmd("true").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		LOG.info("Inspect container {}", inspectContainerResponse.getConfig()
				.getVolumes());

		assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(),
				contains("/var/log"));
		
		assertThat(inspectContainerResponse.getVolumesRW(),
				hasItemInArray(new VolumeRW(volume, AccessMode.rw)));
	}
	
	@Test
	public void createContainerWithReadOnlyVolume() throws DockerException {

		Volume volume = new Volume("/srv/test");
		
		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withVolumes(volume)
				.withCmd("true")
				.exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		LOG.info("Inspect container {}", inspectContainerResponse.getConfig()
				.getVolumes());

		assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(),
				contains("/srv/test"));
		
		assertThat(Arrays.asList(inspectContainerResponse.getVolumesRW()),
				contains(new VolumeRW(volume)));
	}
	
	@Test
	public void createContainerWithVolumesFrom() throws DockerException {

		Volume volume1 = new Volume("/opt/webapp1");
		Volume volume2 = new Volume("/opt/webapp2");

		String container1Name = UUID.randomUUID().toString();
		
		// create a running container with bind mounts
		CreateContainerResponse container1 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999")
				.withName(container1Name)
				.withBinds(new Bind("/src/webapp1", volume1), new Bind("/src/webapp2", volume2))
				.exec();
		LOG.info("Created container1 {}", container1.toString());

		dockerClient.startContainerCmd(container1.getId()).exec();
		LOG.info("Started container1 {}", container1.toString());

		InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(
				container1.getId()).exec();

		assertContainerHasVolumes(inspectContainerResponse1, volume1, volume2);

		// create a second container with volumes from first container
		CreateContainerResponse container2 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999")
				.withVolumesFrom(new VolumesFrom(container1Name)).exec();
		LOG.info("Created container2 {}", container2.toString());

		InspectContainerResponse inspectContainerResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();

		// No volumes are created, the information is just stored in .HostConfig.VolumesFrom
		assertThat(inspectContainerResponse2.getHostConfig().getVolumesFrom(), hasItemInArray(new VolumesFrom(container1Name)));

		// To ensure that the information stored in VolumesFrom really is considered
		// when starting the container, we start it and verify that it has the same 
		// bind mounts as the first container.
		// This is somehow out of scope here, but it helped me to understand how the
		// VolumesFrom feature really works.
		dockerClient.startContainerCmd(container2.getId()).exec();
		LOG.info("Started container2 {}", container2.toString());
		
		inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId()).exec();

		assertThat(inspectContainerResponse2.getHostConfig().getVolumesFrom(), hasItemInArray(new VolumesFrom(container1Name)));
		assertContainerHasVolumes(inspectContainerResponse2, volume1, volume2);
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
				containsInAnyOrder("VARIABLE=success"));

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
		hostConfig.setLinks(new Link("container1", "container1Link"));
		CreateContainerResponse container2 = dockerClient
				.createContainerCmd("busybox").withName("container2").withHostConfig(hostConfig)
				.withCmd("env").exec();
		LOG.info("Created container {}", container2.toString());
		assertThat(container2.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();
		assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[] {new Link("container1","container1Link")}));
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
	
	@Test
	public void createContainerWithEntrypoint() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withName("container")
				.withEntrypoint("sleep", "9999").exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEntrypoint()), contains("sleep", "9999"));

	}

	@Test
	public void createContainerWithExtraHosts() throws DockerException {

		String[] extraHosts = {"dockerhost:127.0.0.1", "otherhost:10.0.0.1"};

		HostConfig hostConfig = new HostConfig();
		hostConfig.setExtraHosts(extraHosts);

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withName("container")
				.withHostConfig(hostConfig).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getExtraHosts()),
				containsInAnyOrder("dockerhost:127.0.0.1", "otherhost:10.0.0.1"));
	}
	
	@Test
	public void createContainerWithDevices() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999")
				.withDevices(new Device("rwm", "/dev/nulo", "/dev/zero"))
				.exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig()
				.getDevices()), contains(new Device("rwm", "/dev/nulo",
				"/dev/zero")));
	}
	
	@Test
	public void createContainerWithPortBindings() throws DockerException {

		ExposedPort tcp22 = ExposedPort.tcp(22);
		ExposedPort tcp23 = ExposedPort.tcp(23);
		
		Ports portBindings = new Ports();
		portBindings.bind(tcp22, Ports.Binding(11022));
		portBindings.bind(tcp23, Ports.Binding(11023));
		portBindings.bind(tcp23, Ports.Binding(11024));

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withCmd("true")
				.withExposedPorts(tcp22, tcp23)
				.withPortBindings(portBindings)
				.exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

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
	public void createContainerWithLinking() throws DockerException {

		CreateContainerResponse container1 = dockerClient
				.createContainerCmd("busybox")
				.withCmd("sleep", "9999")
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
				.createContainerCmd("busybox").withCmd("sleep", "9999")
				.withName("container2")
				.withLinks(new Link("container1", "container1Link"))
				.exec();

		LOG.info("Created container2 {}", container2.toString());
		assertThat(container2.getId(), not(isEmptyString()));
		
		InspectContainerResponse inspectContainerResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();
		LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

		assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
		assertThat(inspectContainerResponse2.getId(), not(isEmptyString()));
		assertThat(inspectContainerResponse2.getHostConfig(),
				is(notNullValue()));
		assertThat(inspectContainerResponse2.getHostConfig().getLinks(),
				is(notNullValue()));
		assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[] { new Link("container1",
				"container1Link") }));
		assertThat(inspectContainerResponse2.getId(),
				startsWith(container2.getId()));
		assertThat(inspectContainerResponse2.getName(), equalTo("/container2"));
		assertThat(inspectContainerResponse2.getImageId(), not(isEmptyString()));
		

	}
	
	@Test
	public void createContainerWithRestartPolicy() throws DockerException {

		RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);
		
		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCmd("sleep", "9999")
				.withRestartPolicy(restartPolicy)
				.exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(),
				is(equalTo(restartPolicy)));
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
	public void createContainerWithNetworkMode() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCmd("true")
				.withNetworkMode("host")
				.exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(),
				is(equalTo("host")));
	}
	
	@Test
	public void createContainerWithMacAddress() throws DockerException {

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withMacAddress("00:80:41:ae:fd:7e")
				.withCmd("true")
				.exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertEquals(inspectContainerResponse.getConfig().getMacAddress(),
				"00:80:41:ae:fd:7e");
	}

	@Test(groups = "ignoreInCircleCi")
	public void createContainerWithULimits() throws DockerException {

		Ulimit[] ulimits = {new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096)};

		HostConfig hostConfig = new HostConfig();
		hostConfig.setUlimits(ulimits);

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox").withName("container")
				.withHostConfig(hostConfig).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getUlimits()),
				containsInAnyOrder(new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096)));

	}
	
}
