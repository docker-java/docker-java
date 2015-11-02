package com.github.dockerjava.jaxrs.command;

import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.AccessMode;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.LogConfig;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Ulimit;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumeRW;
import com.github.dockerjava.api.model.VolumesFrom;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.github.dockerjava.api.model.Capability.MKNOD;
import static com.github.dockerjava.api.model.Capability.NET_ADMIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@Test(groups = "integration")
public class CreateContainerCmdImplTest extends AbstractDockerClientTest {

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
    public void createContainerWithExistingName() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("env")
                .withName(containerName).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        try {
            dockerClient.createContainerCmd("busybox").withCmd("env").withName(containerName).exec();
            Assert.fail("expected ConflictException");
        } catch (ConflictException e) {
        }
    }

    @Test
    public void createContainerWithVolume() throws DockerException {

        Volume volume = new Volume("/var/log");

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withVolumes(volume)
                .withCmd("true").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        AbstractDockerClientTest.LOG.info("Inspect container {}", inspectContainerResponse.getConfig().getVolumes());

        MatcherAssert.assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), Matchers.contains("/var/log"));

        MatcherAssert.assertThat(inspectContainerResponse.getVolumesRW(), Matchers.hasItemInArray(new VolumeRW(volume, AccessMode.rw)));
    }

    @Test
    public void createContainerWithReadOnlyVolume() throws DockerException {

        Volume volume = new Volume("/srv/test");

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withVolumes(volume)
                .withCmd("true").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        AbstractDockerClientTest.LOG.info("Inspect container {}", inspectContainerResponse.getConfig().getVolumes());

        MatcherAssert.assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), Matchers.contains("/srv/test"));

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getVolumesRW()), Matchers.contains(new VolumeRW(volume)));
    }

    @Test
    public void createContainerWithVolumesFrom() throws DockerException {

        Volume volume1 = new Volume("/opt/webapp1");
        Volume volume2 = new Volume("/opt/webapp2");

        String container1Name = UUID.randomUUID().toString();

        // create a running container with bind mounts
        CreateContainerResponse container1 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(container1Name)
                .withBinds(new Bind("/src/webapp1", volume1), new Bind("/src/webapp2", volume2)).exec();
        AbstractDockerClientTest.LOG.info("Created container1 {}", container1.toString());

        dockerClient.startContainerCmd(container1.getId()).exec();
        AbstractDockerClientTest.LOG.info("Started container1 {}", container1.toString());

        InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(container1.getId())
                .exec();

        AbstractDockerClientTest.assertContainerHasVolumes(inspectContainerResponse1, volume1, volume2);

        // create a second container with volumes from first container
        CreateContainerResponse container2 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withVolumesFrom(new VolumesFrom(container1Name)).exec();
        AbstractDockerClientTest.LOG.info("Created container2 {}", container2.toString());

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId())
                .exec();

        // No volumes are created, the information is just stored in .HostConfig.VolumesFrom
        MatcherAssert.assertThat(inspectContainerResponse2.getHostConfig().getVolumesFrom(), Matchers.hasItemInArray(new VolumesFrom(
                container1Name)));

        // To ensure that the information stored in VolumesFrom really is considered
        // when starting the container, we start it and verify that it has the same
        // bind mounts as the first container.
        // This is somehow out of scope here, but it helped me to understand how the
        // VolumesFrom feature really works.
        dockerClient.startContainerCmd(container2.getId()).exec();
        AbstractDockerClientTest.LOG.info("Started container2 {}", container2.toString());

        inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId()).exec();

        MatcherAssert.assertThat(inspectContainerResponse2.getHostConfig().getVolumesFrom(), Matchers.hasItemInArray(new VolumesFrom(
                container1Name)));
        AbstractDockerClientTest.assertContainerHasVolumes(inspectContainerResponse2, volume1, volume2);
    }

    @Test
    public void createContainerWithEnv() throws Exception {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withEnv("VARIABLE=success")
                .withCmd("env").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), Matchers.containsInAnyOrder("VARIABLE=success"));

        dockerClient.startContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(containerLog(container.getId()), Matchers.containsString("VARIABLE=success"));
    }

    @Test
    public void createContainerWithHostname() throws Exception {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withHostName("docker-java")
                .withCmd("env").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(inspectContainerResponse.getConfig().getHostName(), Matchers.equalTo("docker-java"));

        dockerClient.startContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(containerLog(container.getId()), Matchers.containsString("HOSTNAME=docker-java"));
    }

    @Test
    public void createContainerWithName() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withName("container")
                .withCmd("env").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(inspectContainerResponse.getName(), Matchers.equalTo("/container"));

        try {
            dockerClient.createContainerCmd("busybox").withName("container").withCmd("env").exec();
            Assert.fail("Expected ConflictException");
        } catch (ConflictException e) {
        }

    }

    @Test
    public void createContainerWithLink() throws DockerException {

        CreateContainerResponse container1 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName("container1").exec();
        AbstractDockerClientTest.LOG.info("Created container1 {}", container1.toString());
        MatcherAssert.assertThat(container1.getId(), Matchers.not(Matchers.isEmptyString()));

        dockerClient.startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(container1.getId())
                .exec();
        AbstractDockerClientTest.LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());
        MatcherAssert.assertThat(inspectContainerResponse1.getState().isRunning(), Matchers.is(true));

        CreateContainerResponse container2 = dockerClient.createContainerCmd("busybox").withName("container2")
                .withCmd("env").withLinks(new Link("container1", "container1Link")).exec();
        AbstractDockerClientTest.LOG.info("Created container {}", container2.toString());
        MatcherAssert.assertThat(container2.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId())
                .exec();
        MatcherAssert.assertThat(inspectContainerResponse2.getHostConfig().getLinks(), Matchers.equalTo(new Link[] { new Link("container1",
                "container1Link") }));
    }

    @Test
    public void createContainerWithCapAddAndCapDrop() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCapAdd(NET_ADMIN)
                .withCapDrop(MKNOD).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapAdd()), Matchers.contains(NET_ADMIN));

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapDrop()), Matchers.contains(MKNOD));
    }

    @Test
    public void createContainerWithDns() throws DockerException {

        String aDnsServer = "8.8.8.8";
        String anotherDnsServer = "8.8.4.4";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withDns(aDnsServer, anotherDnsServer).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()),
                Matchers.contains(aDnsServer, anotherDnsServer));
    }

    @Test
    public void createContainerWithEntrypoint() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withName("container")
                .withEntrypoint("sleep", "9999").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEntrypoint()), Matchers.contains("sleep", "9999"));

    }

    @Test
    public void createContainerWithExtraHosts() throws DockerException {

        String[] extraHosts = { "dockerhost:127.0.0.1", "otherhost:10.0.0.1" };

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withName("container")
                .withExtraHosts(extraHosts).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getExtraHosts()),
                Matchers.containsInAnyOrder("dockerhost:127.0.0.1", "otherhost:10.0.0.1"));
    }

    @Test
    public void createContainerWithDevices() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withDevices(new Device("rwm", "/dev/nulo", "/dev/zero")).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDevices()), Matchers.contains(new Device("rwm",
                "/dev/nulo", "/dev/zero")));
    }

    @Test
    public void createContainerWithPortBindings() throws DockerException {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Ports.Binding(11022));
        portBindings.bind(tcp23, Ports.Binding(11023));
        portBindings.bind(tcp23, Ports.Binding(11024));

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withExposedPorts(tcp22, tcp23).withPortBindings(portBindings).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()), Matchers.contains(tcp22, tcp23));

        MatcherAssert.assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp22)[0],
                Matchers.is(Matchers.equalTo(Ports.Binding(11022))));

        MatcherAssert.assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[0],
                Matchers.is(Matchers.equalTo(Ports.Binding(11023))));

        MatcherAssert.assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[1],
                Matchers.is(Matchers.equalTo(Ports.Binding(11024))));

    }

    @Test
    public void createContainerWithLinking() throws DockerException {

        CreateContainerResponse container1 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName("container1").exec();

        AbstractDockerClientTest.LOG.info("Created container1 {}", container1.toString());
        MatcherAssert.assertThat(container1.getId(), Matchers.not(Matchers.isEmptyString()));

        dockerClient.startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(container1.getId())
                .exec();
        AbstractDockerClientTest.LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

        MatcherAssert.assertThat(inspectContainerResponse1.getConfig(), Matchers.is(Matchers.notNullValue()));
        MatcherAssert.assertThat(inspectContainerResponse1.getId(), Matchers.not(Matchers.isEmptyString()));
        MatcherAssert.assertThat(inspectContainerResponse1.getId(), Matchers.startsWith(container1.getId()));
        MatcherAssert.assertThat(inspectContainerResponse1.getName(), Matchers.equalTo("/container1"));
        MatcherAssert.assertThat(inspectContainerResponse1.getImageId(), Matchers.not(Matchers.isEmptyString()));
        MatcherAssert.assertThat(inspectContainerResponse1.getState(), Matchers.is(Matchers.notNullValue()));
        MatcherAssert.assertThat(inspectContainerResponse1.getState().isRunning(), Matchers.is(true));

        if (!inspectContainerResponse1.getState().isRunning()) {
            MatcherAssert.assertThat(inspectContainerResponse1.getState().getExitCode(), Matchers.is(Matchers.equalTo(0)));
        }

        CreateContainerResponse container2 = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName("container2").withLinks(new Link("container1", "container1Link")).exec();

        AbstractDockerClientTest.LOG.info("Created container2 {}", container2.toString());
        MatcherAssert.assertThat(container2.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container2.getId())
                .exec();
        AbstractDockerClientTest.LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

        MatcherAssert.assertThat(inspectContainerResponse2.getConfig(), Matchers.is(Matchers.notNullValue()));
        MatcherAssert.assertThat(inspectContainerResponse2.getId(), Matchers.not(Matchers.isEmptyString()));
        MatcherAssert.assertThat(inspectContainerResponse2.getHostConfig(), Matchers.is(Matchers.notNullValue()));
        MatcherAssert.assertThat(inspectContainerResponse2.getHostConfig().getLinks(), Matchers.is(Matchers.notNullValue()));
        MatcherAssert.assertThat(inspectContainerResponse2.getHostConfig().getLinks(), Matchers.equalTo(new Link[] { new Link("container1",
                "container1Link") }));
        MatcherAssert.assertThat(inspectContainerResponse2.getId(), Matchers.startsWith(container2.getId()));
        MatcherAssert.assertThat(inspectContainerResponse2.getName(), Matchers.equalTo("/container2"));
        MatcherAssert.assertThat(inspectContainerResponse2.getImageId(), Matchers.not(Matchers.isEmptyString()));

    }

    @Test
    public void createContainerWithRestartPolicy() throws DockerException {

        RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withRestartPolicy(restartPolicy).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(), Matchers.is(Matchers.equalTo(restartPolicy)));
    }

    @Test
    public void createContainerWithPidMode() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withPidMode("host").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(inspectContainerResponse.getHostConfig().getPidMode(), Matchers.is(Matchers.equalTo("host")));
    }

    /**
     * This tests support for --net option for the docker run command: --net="bridge" Set the Network mode for the
     * container 'bridge': creates a new network stack for the container on the docker bridge 'none': no networking for
     * this container 'container:': reuses another container network stack 'host': use the host network stack inside the
     * container. Note: the host mode gives the container full access to local system services such as D-bus and is
     * therefore considered insecure.
     */
    @Test
    public void createContainerWithNetworkMode() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true")
                .withNetworkMode("host").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(), Matchers.is(Matchers.equalTo("host")));
    }

    @Test
    public void createContainerWithMacAddress() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withMacAddress("00:80:41:ae:fd:7e").withCmd("true").exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        Assert.assertEquals(inspectContainerResponse.getConfig().getMacAddress(), "00:80:41:ae:fd:7e");
    }

    @Test(groups = "ignoreInCircleCi")
    public void createContainerWithULimits() throws DockerException {

        Ulimit[] ulimits = { new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096) };

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withName("container")
                .withUlimits(ulimits).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getUlimits()),
                Matchers.containsInAnyOrder(new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096)));

    }

    @Test(groups = "ignoreInCircleCi")
    public void createContainerWithLabels() throws DockerException {

        Map<String, String> labels = new HashMap<String, String>();
        labels.put("com.github.dockerjava.null", null);
        labels.put("com.github.dockerjava.boolean", "true");

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withLabels(labels).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        // null becomes empty string
        labels.put("com.github.dockerjava.null", "");
        MatcherAssert.assertThat(inspectContainerResponse.getConfig().getLabels(), Matchers.is(Matchers.equalTo(labels)));
    }

    @Test(groups = "ignoreInCircleCi")
    public void createContainerWithLogConfig() throws DockerException {

        LogConfig logConfig = new LogConfig(LogConfig.LoggingType.NONE, null);
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withLogConfig(logConfig).exec();

        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());

        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        // null becomes empty string
        Assert.assertEquals(inspectContainerResponse.getHostConfig().getLogConfig().type, logConfig.type);
    }
}
