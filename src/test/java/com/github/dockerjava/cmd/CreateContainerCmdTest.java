package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.LogConfig;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Ulimit;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.api.model.Capability.MKNOD;
import static com.github.dockerjava.api.model.Capability.NET_ADMIN;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_23;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_24;
import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static com.github.dockerjava.junit.DockerMatchers.mountedVolumes;
import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeThat;

@NotThreadSafe
public class CreateContainerCmdTest extends CmdTest {
    public static final Logger LOG = LoggerFactory.getLogger(CreateContainerCmdTest.class);

    @Test(expected = ConflictException.class)
    public void createContainerWithExistingName() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("env")
                .withName(containerName).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("env").withName(containerName).exec();
    }

    @Test
    public void createContainerWithVolume() throws DockerException {

        Volume volume = new Volume("/var/log");

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withVolumes(volume)
                .withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        LOG.info("Inspect container {}", inspectContainerResponse.getConfig().getVolumes());

        assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), contains("/var/log"));

        assertThat(inspectContainerResponse.getMounts().get(0).getDestination(), equalTo(volume));
        assertThat(inspectContainerResponse.getMounts().get(0).getMode(), equalTo(""));
        assertThat(inspectContainerResponse.getMounts().get(0).getRW(), equalTo(true));
    }

    @Test
    public void createContainerWithReadOnlyVolume() throws DockerException {

        Volume volume = new Volume("/srv/test");

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withVolumes(volume)
                .withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        LOG.info("Inspect container {}", inspectContainerResponse.getConfig().getVolumes());

        assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), contains("/srv/test"));

        assertThat(inspectContainerResponse.getMounts().get(0).getDestination(), equalTo(volume));
        // TODO: Create a read-only volume and test like this
        // assertFalse(inspectContainerResponse.getMounts().get(0).getRW());
    }

    @Test
    public void createContainerWithVolumesFrom() throws DockerException {

        Volume volume1 = new Volume("/opt/webapp1");
        Volume volume2 = new Volume("/opt/webapp2");

        String container1Name = UUID.randomUUID().toString();

        Bind bind1 = new Bind("/src/webapp1", volume1);
        Bind bind2 = new Bind("/src/webapp2", volume2);

        // create a running container with bind mounts
        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withName(container1Name)
                .withBinds(bind1, bind2).exec();
        LOG.info("Created container1 {}", container1.toString());

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();

        assertThat(Arrays.asList(inspectContainerResponse1.getHostConfig().getBinds()), containsInAnyOrder(bind1, bind2));

        assertThat(inspectContainerResponse1, mountedVolumes(containsInAnyOrder(volume1, volume2)));

        // create a second container with volumes from first container
        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withVolumesFrom(new VolumesFrom(container1Name)).exec();
        LOG.info("Created container2 {}", container2.toString());

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();

        // No volumes are created, the information is just stored in .HostConfig.VolumesFrom
        assertThat(inspectContainerResponse2.getHostConfig().getVolumesFrom(), hasItemInArray(new VolumesFrom(
                container1Name)));

        // To ensure that the information stored in VolumesFrom really is considered
        // when starting the container, we start it and verify that it has the same
        // bind mounts as the first container.
        // This is somehow out of scope here, but it helped me to understand how the
        // VolumesFrom feature really works.
        dockerRule.getClient().startContainerCmd(container2.getId()).exec();
        LOG.info("Started container2 {}", container2.toString());

        inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId()).exec();

        assertThat(inspectContainerResponse2.getHostConfig().getVolumesFrom(), hasItemInArray(new VolumesFrom(
                container1Name)));

        assertThat(inspectContainerResponse2, mountedVolumes(containsInAnyOrder(volume1, volume2)));
    }

    @Test
    public void createContainerWithEnv() throws Exception {
        final String testVariable = "VARIABLE=success";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withEnv(testVariable)
                .withCmd("env")
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariable));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariable));
    }

    @Test
    public void createContainerWithHostname() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withHostName("docker-java")
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getHostName(), equalTo("docker-java"));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        assertThat(dockerRule.containerLog(container.getId()), containsString("HOSTNAME=docker-java"));
    }

    @Test(expected = ConflictException.class)
    public void createContainerWithName() throws DockerException {
        String containerName = "container_" + dockerRule.getKind();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName(containerName)
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getName(), equalTo("/" + containerName));

        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName(containerName)
                .withCmd("env")
                .exec();
    }

    @Test
    public void createContainerWithLink() throws DockerException {
        String containerName1 = "containerlink_" + dockerRule.getKind();
        String containerName2 = "container2link_" + dockerRule.getKind();

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withName(containerName1).exec();
        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withName(containerName2)
                .withCmd("env").withLinks(new Link(containerName1, "container1Link")).exec();
        LOG.info("Created container {}", container2.toString());
        assertThat(container2.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[]{new Link(containerName1,
                "container1Link")}));
    }

    @Test
    public void createContainerWithLinkInCustomNetwork() throws DockerException {
        assumeNotSwarm("no network in swarm", dockerRule);
        String containerName1 = "containerlink_" + dockerRule.getKind();
        String networkName = "linkNetcustom" + dockerRule.getKind();

        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd()
                .withName(networkName)
                .exec();

        assertNotNull(createNetworkResponse.getId());

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withNetworkMode(networkName)
                .withCmd("sleep", "9999")
                .withName(containerName1)
                .exec();

        assertThat(container1.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withNetworkMode(networkName)
                .withName("container2")
                .withCmd("env")
                .withLinks(new Link(containerName1, "container1Link"))
                .exec();

        LOG.info("Created container {}", container2.toString());
        assertThat(container2.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();

        ContainerNetwork linkNet = inspectContainerResponse2.getNetworkSettings().getNetworks().get(networkName);
        assertNotNull(linkNet);
        assertThat(linkNet.getLinks(), equalTo(new Link[]{new Link(containerName1, "container1Link")}));
    }

    @Test
    public void createContainerWithCustomIp() throws DockerException {
        assumeNotSwarm("Swarm has no network", dockerRule);

        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd()
                .withIpam(new Network.Ipam()
                        .withConfig(new Network.Ipam.Config()
                                .withSubnet("10.100.101.0/24")))
                .withName("customIpNet")
                .exec();

        assertNotNull(createNetworkResponse.getId());

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withNetworkMode("customIpNet")
                .withCmd("sleep", "9999")
                .withName("container")
                .withIpv4Address("10.100.101.100")
                .exec();

        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId())
                .exec();

        ContainerNetwork customIpNet = inspectContainerResponse.getNetworkSettings().getNetworks().get("customIpNet");
        assertNotNull(customIpNet);
        assertThat(customIpNet.getGateway(), is("10.100.101.1"));
        assertThat(customIpNet.getIpAddress(), is("10.100.101.100"));
    }

    @Test
    public void createContainerWithAlias() throws DockerException {
        assumeNotSwarm("Swarm has no network", dockerRule);

        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd()
                .withName("aliasNet")
                .exec();

        assertNotNull(createNetworkResponse.getId());

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withNetworkMode("aliasNet")
                .withCmd("sleep", "9999")
                .withName("container")
                .withAliases("server")
                .exec();

        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId())
                .exec();

        ContainerNetwork aliasNet = inspectContainerResponse.getNetworkSettings().getNetworks().get("aliasNet");
        assertThat(aliasNet.getAliases(), hasItem("server"));
    }

    @Test
    public void createContainerWithCapAddAndCapDrop() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCapAdd(NET_ADMIN)
                .withCapDrop(MKNOD).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapAdd()), contains(NET_ADMIN));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapDrop()), contains(MKNOD));
    }

    @Test
    public void createContainerWithDns() throws DockerException {

        String aDnsServer = "8.8.8.8";
        String anotherDnsServer = "8.8.4.4";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("true")
                .withDns(aDnsServer, anotherDnsServer).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()),
                contains(aDnsServer, anotherDnsServer));
    }

    @Test
    public void createContainerWithEntrypoint() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withName("container")
                .withEntrypoint("sleep", "9999").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEntrypoint()), contains("sleep", "9999"));

    }

    @Test
    public void createContainerWithExtraHosts() throws DockerException {

        String[] extraHosts = {"dockerhost:127.0.0.1", "otherhost:10.0.0.1"};

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName("containerextrahosts" + dockerRule.getKind())
                .withExtraHosts(extraHosts).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getExtraHosts()),
                containsInAnyOrder("dockerhost:127.0.0.1", "otherhost:10.0.0.1"));
    }

    @Test
    public void createContainerWithDevices() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withDevices(new Device("rwm", "/dev/nulo", "/dev/zero")).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDevices()), contains(new Device("rwm",
                "/dev/nulo", "/dev/zero")));
    }

    @Test
    public void createContainerWithPortBindings() throws DockerException {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.bindPort(11022));
        portBindings.bind(tcp23, Binding.bindPort(11023));
        portBindings.bind(tcp23, Binding.bindPort(11024));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("true")
                .withExposedPorts(tcp22, tcp23).withPortBindings(portBindings).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()), contains(tcp22, tcp23));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp22)[0],
                is(equalTo(Binding.bindPort(11022))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[0],
                is(equalTo(Binding.bindPort(11023))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[1],
                is(equalTo(Binding.bindPort(11024))));

    }

    @Test
    public void createContainerWithLinking() throws DockerException {

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withName("container1").exec();

        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
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

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withName("container2").withLinks(new Link("container1", "container1Link")).exec();

        LOG.info("Created container2 {}", container2.toString());
        assertThat(container2.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();
        LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

        assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getId(), not(isEmptyString()));
        assertThat(inspectContainerResponse2.getHostConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[]{new Link("container1",
                "container1Link")}));
        assertThat(inspectContainerResponse2.getId(), startsWith(container2.getId()));
        assertThat(inspectContainerResponse2.getName(), equalTo("/container2"));
        assertThat(inspectContainerResponse2.getImageId(), not(isEmptyString()));

    }

    @Test
    public void createContainerWithRestartPolicy() throws DockerException {

        RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withRestartPolicy(restartPolicy).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(), is(equalTo(restartPolicy)));
    }

    @Test
    public void createContainerWithPidMode() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("true")
                .withPidMode("host").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getPidMode(), is(equalTo("host")));
    }

    /**
     * This tests support for --net option for the docker run command: --net="bridge" Set the Network mode for the container 'bridge':
     * creates a new network stack for the container on the docker bridge 'none': no networking for this container 'container:': reuses
     * another container network stack 'host': use the host network stack inside the container. Note: the host mode gives the container full
     * access to local system services such as D-bus and is therefore considered insecure.
     */
    @Test
    public void createContainerWithNetworkMode() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("true")
                .withNetworkMode("host").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(), is(equalTo("host")));
    }

    @Test
    public void createContainerWithMacAddress() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withMacAddress("00:80:41:ae:fd:7e").withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getMacAddress(), is("00:80:41:ae:fd:7e"));
    }

    @Test
    public void createContainerWithULimits() throws DockerException {
        String containerName = "containerulimit" + dockerRule.getKind();
        Ulimit[] ulimits = {new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096)};

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName(containerName)
                .withUlimits(ulimits).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getUlimits()),
                containsInAnyOrder(new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096)));

    }

    @Test
    public void createContainerWithLabels() throws DockerException {

        Map<String, String> labels = new HashMap<String, String>();
        labels.put("com.github.dockerjava.null", null);
        labels.put("com.github.dockerjava.Boolean", "true");

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withLabels(labels).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        // null becomes empty string
        labels.put("com.github.dockerjava.null", "");

        // swarm adds 3d label
        assertThat(inspectContainerResponse.getConfig().getLabels(), allOf(
                hasEntry("com.github.dockerjava.null", ""),
                hasEntry("com.github.dockerjava.Boolean", "true")
        ));
    }

    @Test
    public void createContainerWithLogConfig() throws DockerException {

        LogConfig logConfig = new LogConfig(LogConfig.LoggingType.NONE, null);
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withLogConfig(logConfig).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        // null becomes empty string
        assertThat(inspectContainerResponse.getHostConfig().getLogConfig().type, is(logConfig.type));
    }

    /**
     * https://github.com/calavera/docker/blob/3781cde61ff10b1d9114ae5b4c5c1d1b2c20a1ee/integration-cli/docker_cli_run_unix_test.go#L319-L333
     */
    @Test
    public void testWithStopSignal() throws Exception {
        Integer signal = 10; // SIGUSR1 in busybox

        CreateContainerResponse resp = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("/bin/sh", "-c", "trap 'echo \"exit trapped 10\"; exit 10' USR1; while true; do sleep 1; done")
                .withAttachStdin(true)
                .withTty(true)
                .withStopSignal(signal.toString())
                .exec();
        final String containerId = resp.getId();
        assertThat(containerId, not(isEmptyString()));
        dockerRule.getClient().startContainerCmd(containerId).exec();

        InspectContainerResponse inspect = dockerRule.getClient().inspectContainerCmd(containerId).exec();
        assertThat(inspect.getState().getRunning(), is(true));

        dockerRule.getClient().stopContainerCmd(containerId).exec();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        inspect = dockerRule.getClient().inspectContainerCmd(containerId).exec();
        assertThat(inspect.getState().getRunning(), is(false));
        assertThat(inspect.getState().getExitCode(), is(signal));

        StringBuilder stringBuilder = new StringBuilder();
        final StringBuilderLogReader callback = new StringBuilderLogReader(stringBuilder);
        dockerRule.getClient().logContainerCmd(containerId)
                .withStdErr(true)
                .withStdOut(true)
                .withTailAll()
                .exec(callback)
                .awaitCompletion();

        String log = callback.builder.toString();
        assertThat(log, is("exit trapped 10"));
    }

    private static class StringBuilderLogReader extends LogContainerResultCallback {
        public StringBuilder builder;

        public StringBuilderLogReader(StringBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void onNext(Frame item) {
            builder.append(new String(item.getPayload()).trim());
            super.onNext(item);
        }
    }

    @Test
    public void createContainerWithCgroupParent() throws DockerException {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCgroupParent("/parent").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainer = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainer.getHostConfig().getCgroupParent(), is("/parent"));
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void createContainerWithShmSize() throws DockerException {
        HostConfig hostConfig = new HostConfig().withShmSize(96 * FileUtils.ONE_MB);
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(hostConfig).withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getShmSize(), is(hostConfig.getShmSize()));
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void createContainerWithShmPidsLimit() throws DockerException {
        assumeThat("API version should be >= 1.23", dockerRule, isGreaterOrEqual(VERSION_1_23));

        HostConfig hostConfig = new HostConfig().withPidsLimit(2L);
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(hostConfig).withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getPidsLimit(), is(hostConfig.getPidsLimit()));
    }

    @Test
    public void createContainerWithNetworkID() {
        assumeThat("API version should be >= 1.23", dockerRule, isGreaterOrEqual(VERSION_1_24));

        String networkName = "net-" + UUID.randomUUID().toString();
        Map<String, String> labels = new HashMap<>();
        labels.put("com.example.label", "test");
        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd().withName(networkName)
                .withLabels(labels).withAttachable(true).exec();
        String networkId = createNetworkResponse.getId();
        CreateContainerResponse createContainerResponse = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withLabels(labels).withCmd("true").exec();
        String containerId = createContainerResponse.getId();
        dockerRule.getClient().connectToNetworkCmd().withContainerId(containerId).withNetworkId(networkId).exec();
        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(containerId).exec();
        ContainerNetwork containerNetwork = inspectContainerResponse.getNetworkSettings().getNetworks().get(networkName);
        if (containerNetwork == null) {
            // swarm node used network id
            containerNetwork = inspectContainerResponse.getNetworkSettings().getNetworks().get(networkId);
        }
        assertThat(containerNetwork, notNullValue());
    }
}
