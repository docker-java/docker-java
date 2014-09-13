package com.github.dockerjava.core.command;

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

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.*;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;


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

		dockerClient.startContainerCmd(container.getId()).withBinds(new Bind("/src/webapp1", volume1, true), new Bind("/src/webapp2", volume2)).exec();

		dockerClient.waitContainerCmd(container.getId()).exec();

		inspectContainerResponse = dockerClient.inspectContainerCmd(container
				.getId()).exec();

        VolumeBind[] volumeBinds = inspectContainerResponse.getVolumes();
        List<String> volumes = new ArrayList<String>();
        for(VolumeBind bind :volumeBinds){
            volumes.add(bind.getContainerPath());
        }
        assertThat(volumes,	contains(volume1.getPath(), volume2.getPath()));

		assertThat(Arrays.asList(inspectContainerResponse.getVolumesRW()),
				contains(volume1, volume2));

		tmpContainers.add(container.getId());
	}

	@Test
	public void startContainerWithDns() throws DockerException {

		String aDnsServer = "8.8.8.8";
		String anotherDnsServer = "8.8.4.4";

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCmd("true").withDns(aDnsServer, anotherDnsServer).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		dockerClient.startContainerCmd(container.getId()).withDns(aDnsServer, anotherDnsServer).exec();

		inspectContainerResponse = dockerClient.inspectContainerCmd(container
				.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()),
				contains(aDnsServer, anotherDnsServer));

		tmpContainers.add(container.getId());
	}
	
	@Test
	public void startContainerWithPortBindings() throws DockerException {

		ExposedPort tcp22 = ExposedPort.tcp(22);
		ExposedPort tcp23 = ExposedPort.tcp(23);

		CreateContainerResponse container = dockerClient
				.createContainerCmd("busybox")
				.withCmd("true").withExposedPorts(tcp22, tcp23).exec();

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient
				.inspectContainerCmd(container.getId()).exec();

		Ports portBindings = new Ports();
		portBindings.bind(tcp22, Ports.Binding(11022));
		portBindings.bind(tcp23, Ports.Binding(11023));

		dockerClient.startContainerCmd(container.getId()).withPortBindings(portBindings).exec();

		inspectContainerResponse = dockerClient.inspectContainerCmd(container
				.getId()).exec();

		assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()),
				contains(tcp22, tcp23));

		assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp22),
				is(equalTo(Ports.Binding(11022))));

		assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23),
				is(equalTo(Ports.Binding(11023))));

		tmpContainers.add(container.getId());
	}

	@Test
	public void startContainerWithLinking() throws DockerException {

		CreateContainerResponse container1 = dockerClient
				.createContainerCmd("busybox").withCmd("sleep", "9999").withName("container1").exec();

		LOG.info("Created container1 {}", container1.toString());
		assertThat(container1.getId(), not(isEmptyString()));
		tmpContainers.add(container1.getId());

		dockerClient.startContainerCmd(container1.getId()).exec();

		InspectContainerResponse inspectContainerResponse1 = dockerClient
				.inspectContainerCmd(container1.getId()).exec();
		LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

		assertThat(inspectContainerResponse1.getConfig(), is(notNullValue()));
		assertThat(inspectContainerResponse1.getId(), not(isEmptyString()));
		assertThat(inspectContainerResponse1.getId(), startsWith(container1.getId()));
		assertThat(inspectContainerResponse1.getName(), equalTo("/container1"));
		assertThat(inspectContainerResponse1.getImageId(), not(isEmptyString()));
		assertThat(inspectContainerResponse1.getState(), is(notNullValue()));
		assertThat(inspectContainerResponse1.getState().isRunning(), is(true));

		if (!inspectContainerResponse1.getState().isRunning()) {
			assertThat(inspectContainerResponse1.getState().getExitCode(),
					is(equalTo(0)));
		}

		CreateContainerResponse container2 = dockerClient
				.createContainerCmd("busybox").withCmd("true").withName("container2").exec();

		LOG.info("Created container2 {}", container2.toString());
		assertThat(container2.getId(), not(isEmptyString()));
		tmpContainers.add(container2.getId());

		dockerClient.startContainerCmd(container2.getId()).withLinks(new Link("container1", "container1Link")).exec();

		InspectContainerResponse inspectContainerResponse2 = dockerClient
				.inspectContainerCmd(container2.getId()).exec();
		LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

		assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
		assertThat(inspectContainerResponse2.getId(), not(isEmptyString()));
		assertThat(inspectContainerResponse2.getHostConfig(), is(notNullValue()));
		assertThat(inspectContainerResponse2.getHostConfig().getLinks(), is(notNullValue()));
		assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new String[] {"/container1:/container2/container1Link"}));
		assertThat(inspectContainerResponse2.getId(), startsWith(container2.getId()));
		assertThat(inspectContainerResponse2.getName(), equalTo("/container2"));
		assertThat(inspectContainerResponse2.getImageId(), not(isEmptyString()));
		assertThat(inspectContainerResponse2.getState(), is(notNullValue()));
		assertThat(inspectContainerResponse2.getState().isRunning(), is(true));

	}


	@Test
	public void startContainer() throws DockerException {

		CreateContainerResponse container = dockerClient
			.createContainerCmd("busybox").withCmd(new String[] { "top" }).exec();

		LOG.info("Created container {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		tmpContainers.add(container.getId());

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

	
	/**
	 * This tests support for --net option for the docker run command:
	 * --net="bridge" Set the Network mode for the container
	 * 'bridge': creates a new network stack for the container on the docker bridge
	 * 'none': no networking for this container
	 * 'container:': reuses another container network stack
	 * 'host': use the host network stack inside the container.
	 *  Note: the host mode gives the container full access to local system services such as D-bus and is therefore considered insecure.
	 */
	@Test
    public void startContainerWithNetworkMode() throws DockerException {

        CreateContainerResponse container = dockerClient
                .createContainerCmd("busybox")
                .withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));
        
        tmpContainers.add(container.getId());

        InspectContainerResponse inspectContainerResponse = dockerClient
                .inspectContainerCmd(container.getId()).exec();

        dockerClient.startContainerCmd(container.getId()).withNetworkMode("host").exec();

        inspectContainerResponse = dockerClient.inspectContainerCmd(container
                .getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(),
                is(equalTo("host")));
    
    }
	
	@Test
    public void startContainerWithCapAddAndCapDrop() throws DockerException {

        CreateContainerResponse container = dockerClient
                .createContainerCmd("busybox")
                .withCmd("sleep", "9999").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        tmpContainers.add(container.getId());

        dockerClient.startContainerCmd(container.getId())
        	.withCapAdd("NET_ADMIN")
        	.withCapDrop("MKNOD").exec();

        InspectContainerResponse inspectContainerResponse  = dockerClient.inspectContainerCmd(container
                .getId()).exec();
        
        assertThat(inspectContainerResponse.getState().isRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapAdd()),
                contains("NET_ADMIN"));
        
        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapDrop()),
                contains("MKNOD"));  
    }
	
	@Test
    public void startContainerWithDevices() throws DockerException {

        CreateContainerResponse container = dockerClient
                .createContainerCmd("busybox")
                .withCmd("sleep", "9999").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        tmpContainers.add(container.getId());

        dockerClient.startContainerCmd(container.getId())
        	.withDevices(new Device("rwm", "/dev/nulo", "/dev/zero"))
        	.exec();

        InspectContainerResponse inspectContainerResponse  = dockerClient.inspectContainerCmd(container
                .getId()).exec();
        
        assertThat(inspectContainerResponse.getState().isRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDevices()),
                contains(new Device("rwm", "/dev/nulo", "/dev/zero")));
    }
	
	@Test
    public void startContainerWithRestartPolicy() throws DockerException {

        CreateContainerResponse container = dockerClient
                .createContainerCmd("busybox")
                .withCmd("sleep", "9999").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        tmpContainers.add(container.getId());

        RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);
        
        dockerClient.startContainerCmd(container.getId())
        	.withRestartPolicy(restartPolicy)
        	.exec();

        InspectContainerResponse inspectContainerResponse  = dockerClient.inspectContainerCmd(container
                .getId()).exec();
        
        assertThat(inspectContainerResponse.getState().isRunning(), is(true));

        assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(),
                is(equalTo(restartPolicy)));
    }

}
