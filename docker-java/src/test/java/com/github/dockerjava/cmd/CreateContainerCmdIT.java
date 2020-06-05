package com.github.dockerjava.cmd;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
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
import com.github.dockerjava.junit.DockerAssume;
import com.github.dockerjava.junit.PrivateRegistryRule;
import com.github.dockerjava.utils.TestUtils;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.io.FileUtils;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.api.model.Capability.MKNOD;
import static com.github.dockerjava.api.model.Capability.NET_ADMIN;
import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_23;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_24;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static com.github.dockerjava.junit.DockerMatchers.mountedVolumes;
import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assume.assumeThat;

@NotThreadSafe
public class CreateContainerCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(CreateContainerCmdIT.class);

    @ClassRule
    public static PrivateRegistryRule REGISTRY = new PrivateRegistryRule();

    @Rule
    public TemporaryFolder tempDir = new TemporaryFolder(new File("target/"));

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test(expected = ConflictException.class)
    public void createContainerWithExistingName() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("env")
                .withName(containerName).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("env").withName(containerName).exec();
    }

    @Test
    public void createContainerWithVolume() throws DockerException {

        Volume volume = new Volume("/var/log");

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withVolumes(volume)
                .withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

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

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        LOG.info("Inspect container {}", inspectContainerResponse.getConfig().getVolumes());

        assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), contains("/srv/test"));

        assertThat(inspectContainerResponse.getMounts().get(0).getDestination(), equalTo(volume));
        // TODO: Create a read-only volume and test like this
        // assertFalse(inspectContainerResponse.getMounts().get(0).getRW());
    }

    @Test
    public void createContainerWithVolumesFrom() throws DockerException {
        String container1Name = UUID.randomUUID().toString();
        CreateVolumeResponse volume1Info = dockerRule.getClient().createVolumeCmd().exec();
        CreateVolumeResponse volume2Info = dockerRule.getClient().createVolumeCmd().exec();

        Volume volume1 = new Volume("/src/webapp1");
        Volume volume2 = new Volume("/src/webapp2");
        Bind bind1 = new Bind(volume1Info.getName(), volume1);
        Bind bind2 = new Bind(volume2Info.getName(), volume2);

        // create a running container with bind mounts
        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sleep", "9999")
                .withName(container1Name)
                .withHostConfig(newHostConfig()
                        .withBinds(bind1, bind2))
                .exec();

        LOG.info("Created container1 {}", container1.toString());

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();

        assertThat(Arrays.asList(inspectContainerResponse1.getHostConfig().getBinds()), containsInAnyOrder(bind1, bind2));

        assertThat(inspectContainerResponse1, mountedVolumes(containsInAnyOrder(volume1, volume2)));

        // create a second container with volumes from first container
        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withVolumesFrom(new VolumesFrom(container1Name)))
                .exec();

        LOG.info("Created container2 {}", container2.toString());

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();

        // No volumes are created, the information is just stored in .HostConfig.VolumesFrom
        assertThat(inspectContainerResponse2.getHostConfig().getVolumesFrom(),
                hasItemInArray(new VolumesFrom(container1Name)));
        assertThat(inspectContainerResponse1, mountedVolumes(containsInAnyOrder(volume1, volume2)));

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

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariable));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariable));
    }

    @Test
    public void createContainerWithEnvAdditive() throws Exception {

        final String testVariable1 = "VARIABLE1=success1";
        final String testVariable2 = "VARIABLE2=success2";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withEnv(testVariable1)
                .withEnv(testVariable2)
                .withCmd("env")
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), not(hasItem(testVariable1)));
        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariable2));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        assertThat(dockerRule.containerLog(container.getId()), not(containsString(testVariable1)));
        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariable2));
    }

    @Test
    public void createContainerWithEnvAdditiveMap() throws Exception {
        final String[] testVariables1 = {"VARIABLE1=success1", "VARIABLE2=success2"};
        final String[] testVariables2 = {"VARIABLE3=success3", "VARIABLE4=success4"};

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withEnv(testVariables1)
                .withEnv(testVariables2)
                .withCmd("env")
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), not(hasItem(testVariables1[0])));
        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), not(hasItem(testVariables1[1])));

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariables2[0]));
        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariables2[1]));


        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        assertThat(dockerRule.containerLog(container.getId()), not(containsString(testVariables1[0])));
        assertThat(dockerRule.containerLog(container.getId()), not(containsString(testVariables1[1])));

        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariables2[0]));
        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariables2[1]));
    }

    @Test
    public void createContainerWithEnvAsVararg() throws Exception {

        final String testVariable1 = "VARIABLE1=success1";
        final String testVariable2 = "VARIABLE2=success2";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withEnv(testVariable1, testVariable2)
                .withCmd("env")
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariable1));
        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariable2));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariable1));
        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariable2));
    }

    @Test
    public void createContainerWithEnvAsMap() throws Exception {
        final String[] testVariables = {"VARIABLE1=success1", "VARIABLE2=success2"};

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withEnv(testVariables)
                .withCmd("env")
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariables[0]));
        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEnv()), hasItem(testVariables[1]));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariables[0]));
        assertThat(dockerRule.containerLog(container.getId()), containsString(testVariables[1]));
    }

    @Test
    public void createContainerWithHostname() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withHostName("docker-java")
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

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

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getName(), equalTo("/" + containerName));

        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName(containerName)
                .withCmd("env")
                .exec();
    }

    @Test
    public void createContainerWithLink() throws DockerException {
        String containerName1 = "containerWithlink_" + dockerRule.getKind();
        String containerName2 = "container2Withlink_" + dockerRule.getKind();

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withName(containerName1).exec();
        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withName(containerName2)
                .withCmd("env")
                .withHostConfig(newHostConfig()
                        .withLinks(new Link(containerName1, "container1Link")))
                .exec();
        LOG.info("Created container {}", container2.toString());
        assertThat(container2.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[]{new Link(containerName1,
                "container1Link")}));
    }

    @Test
    public void createContainerWithMemorySwappiness() throws DockerException {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withMemorySwappiness(42L))
                .exec();
        assertThat(container.getId(), not(is(emptyString())));
        LOG.info("Created container {}", container.toString());

        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        LOG.info("Started container {}", container.toString());

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient()
                .inspectContainerCmd(container.getId())
                .exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());
        assertSame(42L, inspectContainerResponse.getHostConfig().getMemorySwappiness());
    }

    @Test
    public void createContainerWithLinkInCustomNetwork() throws DockerException {
        String containerName1 = "containerCustomlink_" + dockerRule.getKind();
        String containerName2 = "containerCustom2link_" + dockerRule.getKind();
        String networkName = "linkNetcustom" + dockerRule.getKind();

        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd()
                .withName(networkName)
                .withDriver("bridge")
                .exec();

        assertNotNull(createNetworkResponse.getId());

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withNetworkMode(networkName))
                .withCmd("sleep", "9999")
                .withName(containerName1)
                .exec();

        assertThat(container1.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withLinks(new Link(containerName1, containerName1 + "Link"))
                        .withNetworkMode(networkName))
                .withName(containerName2)
                .withCmd("env")
                .exec();

        LOG.info("Created container {}", container2.toString());
        assertThat(container2.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();

        ContainerNetwork linkNet = inspectContainerResponse2.getNetworkSettings().getNetworks().get(networkName);
        assertNotNull(linkNet);
        assertThat(linkNet.getLinks(), equalTo(new Link[]{new Link(containerName1, containerName1 + "Link")}));
    }

    @Test
    public void createContainerWithCustomIp() throws DockerException {
        String containerName1 = "containerCustomIplink_" + dockerRule.getKind();
        String networkName = "customIpNet" + dockerRule.getKind();
        String subnetPrefix = getFactoryType().getSubnetPrefix() + "101";

        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd()
                .withIpam(new Network.Ipam()
                        .withConfig(new Network.Ipam.Config()
                                .withSubnet(subnetPrefix + ".0/24")))
                .withDriver("bridge")
                .withName(networkName)
                .exec();

        assertNotNull(createNetworkResponse.getId());

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withNetworkMode(networkName))
                .withCmd("sleep", "9999")
                .withName(containerName1)
                .withIpv4Address(subnetPrefix + ".100")
                .exec();

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient()
                .inspectContainerCmd(container.getId())
                .exec();

        ContainerNetwork customIpNet = inspectContainerResponse.getNetworkSettings().getNetworks().get(networkName);
        assertNotNull(customIpNet);
        assertThat(customIpNet.getGateway(), is(subnetPrefix + ".1"));
        assertThat(customIpNet.getIpAddress(), is(subnetPrefix + ".100"));
    }

    @Test
    public void createContainerWithAlias() throws DockerException {
        String containerName1 = "containerAlias_" + dockerRule.getKind();
        String networkName = "aliasNet" + dockerRule.getKind();

        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd()
                .withName(networkName)
                .withDriver("bridge")
                .exec();

        assertNotNull(createNetworkResponse.getId());

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withNetworkMode(networkName))
                .withCmd("sleep", "9999")
                .withName(containerName1)
                .withAliases("server" + dockerRule.getKind())
                .exec();

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId())
                .exec();

        ContainerNetwork aliasNet = inspectContainerResponse.getNetworkSettings().getNetworks().get(networkName);
        assertThat(aliasNet.getAliases(), hasItem("server" + dockerRule.getKind()));
    }

    @Test
    public void createContainerWithCapAddAndCapDrop() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withCapAdd(NET_ADMIN)
                        .withCapDrop(MKNOD))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapAdd()), contains(NET_ADMIN));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapDrop()), contains(MKNOD));
    }

    @Test
    public void createContainerWithDns() throws DockerException {

        String aDnsServer = "8.8.8.8";
        String anotherDnsServer = "8.8.4.4";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("true")
                .withHostConfig(newHostConfig()
                        .withDns(aDnsServer, anotherDnsServer))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()),
                contains(aDnsServer, anotherDnsServer));
    }

    @Test
    public void createContainerWithEntrypoint() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName("containerEntrypoint" + dockerRule.getKind())
                .withEntrypoint("sleep", "9999").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getEntrypoint()), contains("sleep", "9999"));

    }

    @Test
    public void createContainerWithExtraHosts() throws DockerException {

        String[] extraHosts = {"dockerhost:127.0.0.1", "otherhost:10.0.0.1"};

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName("containerextrahosts" + dockerRule.getKind())
                .withHostConfig(newHostConfig()
                        .withExtraHosts(extraHosts)).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getExtraHosts()),
                containsInAnyOrder("dockerhost:127.0.0.1", "otherhost:10.0.0.1"));
    }

    @Test
    public void createContainerWithDevices() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withDevices(new Device("rwm", "/dev/nulo", "/dev/zero")))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDevices()), contains(new Device("rwm",
                "/dev/nulo", "/dev/zero")));
    }

    @Test
    public void createContainerWithPortBindings() throws DockerException {
        int baseport = 10_000 + (getFactoryType().ordinal() * 1000);

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.bindPort(baseport + 22));
        portBindings.bind(tcp23, Binding.bindPort(baseport + 23));
        portBindings.bind(tcp23, Binding.bindPort(baseport + 24));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("true")
                .withExposedPorts(tcp22, tcp23)
                .withHostConfig(newHostConfig()
                        .withPortBindings(portBindings))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()), contains(tcp22, tcp23));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp22)[0],
                is(equalTo(Binding.bindPort(baseport + 22))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[0],
                is(equalTo(Binding.bindPort(baseport + 23))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[1],
                is(equalTo(Binding.bindPort(baseport + 24))));

    }

    @Test
    public void createContainerWithLinking() throws DockerException {
        String containerName1 = "containerWithlinking_" + dockerRule.getKind();
        String containerName2 = "container2Withlinking_" + dockerRule.getKind();

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sleep", "9999")
                .withName(containerName1).exec();

        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

        assertThat(inspectContainerResponse1.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getId(), not(is(emptyString())));
        assertThat(inspectContainerResponse1.getId(), startsWith(container1.getId()));
        assertThat(inspectContainerResponse1.getName(), equalTo("/" + containerName1));
        assertThat(inspectContainerResponse1.getImageId(), not(is(emptyString())));
        assertThat(inspectContainerResponse1.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        if (!inspectContainerResponse1.getState().getRunning()) {
            assertThat(inspectContainerResponse1.getState().getExitCode(), is(equalTo(0)));
        }

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withName(containerName2)
                .withHostConfig(newHostConfig()
                        .withLinks(new Link(containerName1, containerName1 + "Link")))
                .exec();

        LOG.info("Created container2 {}", container2.toString());
        assertThat(container2.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();
        LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

        assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getId(), not(is(emptyString())));
        assertThat(inspectContainerResponse2.getHostConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[]{new Link(containerName1,
                containerName1 + "Link")}));
        assertThat(inspectContainerResponse2.getId(), startsWith(container2.getId()));
        assertThat(inspectContainerResponse2.getName(), equalTo("/" + containerName2));
        assertThat(inspectContainerResponse2.getImageId(), not(is(emptyString())));

    }

    @Test
    public void createContainerWithRestartPolicy() throws DockerException {

        RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withHostConfig(newHostConfig().withRestartPolicy(restartPolicy)).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(), is(equalTo(restartPolicy)));
    }

    @Test
    public void createContainerWithPidMode() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("true")
                .withHostConfig(newHostConfig().withPidMode("host")).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

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
                .withHostConfig(newHostConfig()
                        .withNetworkMode("host"))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(), is(equalTo("host")));
    }

    @Test
    public void createContainerWithMacAddress() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withMacAddress("00:80:41:ae:fd:7e").withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getMacAddress(), is("00:80:41:ae:fd:7e"));
    }

    @Test
    public void createContainerWithULimits() throws DockerException {
        String containerName = "containerulimit" + dockerRule.getKind();
        Ulimit[] ulimits = {new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096)};

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName(containerName)
                .withHostConfig(newHostConfig()
                        .withUlimits(ulimits))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getUlimits()),
                containsInAnyOrder(new Ulimit("nproc", 709, 1026), new Ulimit("nofile", 1024, 4096)));

    }

    @Test
    public void createContainerWithIntegerBoundsExceedingULimit() throws DockerException {
        String containerName = "containercoreulimit" + dockerRule.getKind();
        Ulimit[] ulimits = {new Ulimit("core", 99999999998L, 99999999999L)};

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName(containerName)
                .withHostConfig(newHostConfig()
                        .withUlimits(ulimits))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getUlimits()),
                contains(new Ulimit("core", 99999999998L, 99999999999L)));

    }

    @Test
    public void createContainerWithLabels() throws DockerException {

        Map<String, String> labels = new HashMap<>();
        labels.put("com.github.dockerjava.null", null);
        labels.put("com.github.dockerjava.Boolean", "true");

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withLabels(labels).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

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
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withHostConfig(newHostConfig()
                        .withLogConfig(logConfig))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

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
        assertThat(containerId, not(is(emptyString())));
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
        assertThat(log.trim(), is("exit trapped 10"));
    }

    private static class StringBuilderLogReader extends ResultCallback.Adapter<Frame> {
        public StringBuilder builder;

        public StringBuilderLogReader(StringBuilder builder) {
            this.builder = builder;
        }

        @Override
        public void onNext(Frame item) {
            builder.append(new String(item.getPayload()));
            super.onNext(item);
        }
    }

    @Test
    public void createContainerWithCgroupParent() throws DockerException {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withHostConfig(newHostConfig()
                        .withCgroupParent("/parent"))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

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

        assertThat(container.getId(), not(is(emptyString())));

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

        assertThat(container.getId(), not(is(emptyString())));

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

    @Test
    public void createContainerFromPrivateRegistryWithValidAuth() throws Exception {
        DockerAssume.assumeSwarm(dockerRule.getClient());

        AuthConfig authConfig = REGISTRY.getAuthConfig();

        String imgName = REGISTRY.createPrivateImage("create-container-with-valid-auth");

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(imgName)
                .withAuthConfig(authConfig)
                .exec();

        assertThat(container.getId(), is(notNullValue()));
    }

    @Test
    public void createContainerFromPrivateRegistryWithNoAuth() throws Exception {
        AuthConfig authConfig = REGISTRY.getAuthConfig();

        String imgName = REGISTRY.createPrivateImage("create-container-with-no-auth");

        if (TestUtils.isSwarm(dockerRule.getClient())) {
            exception.expect(instanceOf(InternalServerErrorException.class));
        } else {
            exception.expect(instanceOf(NotFoundException.class));
        }

        dockerRule.getClient().createContainerCmd(imgName)
                .exec();
    }

    @Test
    public void createContainerWithTmpFs() throws DockerException {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999")
                .withHostConfig(new HostConfig().withTmpFs(Collections.singletonMap("/tmp", "rw,noexec,nosuid,size=50m"))).exec();

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getHostConfig().getTmpFs().get("/tmp"), equalTo("rw,noexec,nosuid,size=50m"));
    }

    @Test
    public void createContainerWithNanoCPUs() throws DockerException {
        Long nanoCPUs = 1000000000L;

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
            .withCmd("sleep", "9999")
            .withHostConfig(newHostConfig()
                .withNanoCPUs(nanoCPUs))
            .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getNanoCPUs(), is(nanoCPUs));
    }
}
