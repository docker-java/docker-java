package com.github.dockerjava.core.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.client.AbstractDockerClientTest;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.github.dockerjava.api.model.AccessMode.ro;
import static com.github.dockerjava.api.model.Capability.MKNOD;
import static com.github.dockerjava.api.model.Capability.NET_ADMIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

@Test(groups = "integration")
public class StartContainerCmdImplTest extends AbstractDockerClientTest {

    @BeforeTest
    public void beforeTest() throws Exception {
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

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withVolumes(volume1, volume2)
                .withCmd("true").withBinds(new Bind("/src/webapp1", volume1, ro), new Bind("/src/webapp2", volume2))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), contains("/opt/webapp1", "/opt/webapp2"));

        dockerClient.startContainerCmd(container.getId()).exec();

        dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback()).awaitStatusCode();

        inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse, mountedVolumes(containsInAnyOrder(volume1, volume2)));

        final List<InspectContainerResponse.Mount> mounts = inspectContainerResponse.getMounts();

        assertThat(mounts, hasSize(2));

        final InspectContainerResponse.Mount mount1 = new InspectContainerResponse.Mount()
                .withRw(false).withMode("ro").withDestination(volume1).withSource("/src/webapp1");
        final InspectContainerResponse.Mount mount2 = new InspectContainerResponse.Mount()
                .withRw(true).withMode("rw").withDestination(volume2).withSource("/src/webapp2");

        assertThat(mounts, containsInAnyOrder(mount1, mount2));
    }

    @Test
    public void startContainerWithVolumesFrom() throws DockerException {

        Volume volume1 = new Volume("/opt/webapp1");
        Volume volume2 = new Volume("/opt/webapp2");

        String container1Name = UUID.randomUUID().toString();

        CreateContainerResponse container1 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(container1Name)
                .withBinds(new Bind("/src/webapp1", volume1), new Bind("/src/webapp2", volume2)).exec();
        LOG.info("Created container1 {}", container1.toString());

        dockerClient.startContainerCmd(container1.getId()).exec();
        LOG.info("Started container1 {}", container1.toString());

        InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(container1.getId())
                .exec();

        assertThat(inspectContainerResponse1, mountedVolumes(containsInAnyOrder(volume1, volume2)));

        CreateContainerResponse container2 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withVolumesFrom(new VolumesFrom(container1Name)).exec();
        LOG.info("Created container2 {}", container2.toString());

        dockerClient.startContainerCmd(container2.getId()).exec();
        LOG.info("Started container2 {}", container2.toString());

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId())
                .exec();

        assertThat(inspectContainerResponse2, mountedVolumes(containsInAnyOrder(volume1, volume2)));
    }

    @Test
    public void startContainerWithDns() throws DockerException {

        String aDnsServer = "8.8.8.8";
        String anotherDnsServer = "8.8.4.4";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withDns(aDnsServer, anotherDnsServer).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()),
                contains(aDnsServer, anotherDnsServer));
    }

    @Test
    public void startContainerWithDnsSearch() throws DockerException {

        String dnsSearch = "example.com";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withDnsSearch(dnsSearch).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDnsSearch()), contains(dnsSearch));
    }

    @Test
    public void startContainerWithPortBindings() throws DockerException {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.bindPort(11022));
        portBindings.bind(tcp23, Binding.bindPort(11023));
        portBindings.bind(tcp23, Binding.bindPort(11024));

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withExposedPorts(tcp22, tcp23).withPortBindings(portBindings).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()), contains(tcp22, tcp23));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp22)[0],
                is(equalTo(Binding.bindPort(11022))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[0],
                is(equalTo(Binding.bindPort(11023))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[1],
                is(equalTo(Binding.bindPort(11024))));

    }

    @Test
    public void startContainerWithRandomPortBindings() throws DockerException {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.empty());
        portBindings.bind(tcp23, Binding.empty());

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withExposedPorts(tcp22, tcp23).withPortBindings(portBindings).withPublishAllPorts(true).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()), contains(tcp22, tcp23));

        assertThat(inspectContainerResponse.getNetworkSettings().getPorts().getBindings().get(tcp22)[0].getHostPortSpec(),
                is(not(equalTo(String.valueOf(tcp22.getPort())))));

        assertThat(inspectContainerResponse.getNetworkSettings().getPorts().getBindings().get(tcp23)[0].getHostPortSpec(),
                is(not(equalTo(String.valueOf(tcp23.getPort())))));

    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void startContainerWithConflictingPortBindings() throws DockerException {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.bindPort(11022));
        portBindings.bind(tcp23, Binding.bindPort(11022));

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withExposedPorts(tcp22, tcp23).withPortBindings(portBindings).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();
    }

    @Test
    public void startContainerWithLinkingDeprecated() throws DockerException {

        CreateContainerResponse container1 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName("container1").exec();

        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

        assertThat(inspectContainerResponse1.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getId(), not(isEmptyString()));
        assertThat(inspectContainerResponse1.getId(), startsWith(container1.getId()));
        assertThat(inspectContainerResponse1.getName(), equalTo("/container1"));
        assertThat(inspectContainerResponse1.getImageId(), not(isEmptyString()));
        assertThat(inspectContainerResponse1.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        if (!inspectContainerResponse1.getState().getRunning()) {
            assertThat(inspectContainerResponse1.getState().getExitCode(), is(equalTo(0)));
        }

        CreateContainerResponse container2 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName("container2").withLinks(new Link("container1", "container1Link")).exec();

        LOG.info("Created container2 {}", container2.toString());
        assertThat(container2.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container2.getId()).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId())
                .exec();
        LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

        assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getId(), not(isEmptyString()));
        assertThat(inspectContainerResponse2.getHostConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[] {new Link("container1",
                "container1Link")}));
        assertThat(inspectContainerResponse2.getId(), startsWith(container2.getId()));
        assertThat(inspectContainerResponse2.getName(), equalTo("/container2"));
        assertThat(inspectContainerResponse2.getImageId(), not(isEmptyString()));
        assertThat(inspectContainerResponse2.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getState().getRunning(), is(true));

    }

    @Test
    public void startContainerWithLinking() throws DockerException {

        CreateContainerResponse container1 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName("container1").exec();

        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

        assertThat(inspectContainerResponse1.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getId(), not(isEmptyString()));
        assertThat(inspectContainerResponse1.getId(), startsWith(container1.getId()));
        assertThat(inspectContainerResponse1.getName(), equalTo("/container1"));
        assertThat(inspectContainerResponse1.getImageId(), not(isEmptyString()));
        assertThat(inspectContainerResponse1.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        if (!inspectContainerResponse1.getState().getRunning()) {
            assertThat(inspectContainerResponse1.getState().getExitCode(), is(equalTo(0)));
        }

        CreateContainerResponse container2 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName("container2").withLinks(new Link("container1", "container1Link")).exec();

        LOG.info("Created container2 {}", container2.toString());
        assertThat(container2.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container2.getId()).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId())
                .exec();
        LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

        assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getId(), not(isEmptyString()));
        assertThat(inspectContainerResponse2.getHostConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[] {new Link("container1",
                "container1Link")}));
        assertThat(inspectContainerResponse2.getId(), startsWith(container2.getId()));
        assertThat(inspectContainerResponse2.getName(), equalTo("/container2"));
        assertThat(inspectContainerResponse2.getImageId(), not(isEmptyString()));
        assertThat(inspectContainerResponse2.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getState().getRunning(), is(true));

    }

    @Test
    public void startContainer() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd(new String[] {"top"})
                .exec();

        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse.getId(), not(isEmptyString()));

        assertThat(inspectContainerResponse.getId(), startsWith(container.getId()));

        assertThat(inspectContainerResponse.getImageId(), not(isEmptyString()));
        assertThat(inspectContainerResponse.getState(), is(notNullValue()));

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        if (!inspectContainerResponse.getState().getRunning()) {
            assertThat(inspectContainerResponse.getState().getExitCode(), is(equalTo(0)));
        }
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testStartNonExistingContainer() throws DockerException {

            dockerClient.startContainerCmd("non-existing").exec();
    }

    /**
     * This tests support for --net option for the docker run command: --net="bridge" Set the Network mode for the container 'bridge':
     * creates a new network stack for the container on the docker bridge 'none': no networking for this container 'container:': reuses
     * another container network stack 'host': use the host network stack inside the container. Note: the host mode gives the container full
     * access to local system services such as D-bus and is therefore considered insecure.
     */
    @Test
    public void startContainerWithNetworkMode() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withNetworkMode("host").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(), is(equalTo("host")));
    }

    @Test
    public void startContainerWithCapAddAndCapDrop() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withCapAdd(NET_ADMIN).withCapDrop(MKNOD).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapAdd()), contains(NET_ADMIN));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapDrop()), contains(MKNOD));
    }

    @Test
    public void startContainerWithDevices() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withDevices(new Device("rwm", "/dev/nulo", "/dev/zero")).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDevices()), contains(new Device("rwm",
                "/dev/nulo", "/dev/zero")));
    }

    @Test
    public void startContainerWithExtraHosts() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withExtraHosts("dockerhost:127.0.0.1").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getExtraHosts()),
                contains("dockerhost:127.0.0.1"));
    }

    @Test
    public void startContainerWithRestartPolicy() throws DockerException {

        RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withRestartPolicy(restartPolicy).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(), is(equalTo(restartPolicy)));
    }

    @Test
    public void existingHostConfigIsPreservedByBlankStartCmd() throws DockerException {

        String dnsServer = "8.8.8.8";

        // prepare a container with custom DNS
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withDns(dnsServer)
                .withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        // start container _without_any_customization_ (important!)
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        // The DNS setting survived.
        assertThat(inspectContainerResponse.getHostConfig().getDns(), is(notNullValue()));
        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()), contains(dnsServer));
    }

    @Test
    public void anUnconfiguredCommandSerializesToEmptyJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        StartContainerCmd command = dockerClient.startContainerCmd("");
        assertThat(objectMapper.writeValueAsString(command), is("{}"));
    }
}
