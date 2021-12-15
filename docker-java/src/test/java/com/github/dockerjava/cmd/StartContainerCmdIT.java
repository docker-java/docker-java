package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Device;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Link;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.api.model.RestartPolicy;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.VolumesFrom;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.github.dockerjava.api.model.AccessMode.ro;
import static com.github.dockerjava.api.model.Capability.MKNOD;
import static com.github.dockerjava.api.model.Capability.NET_ADMIN;
import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static com.github.dockerjava.junit.DockerMatchers.mountedVolumes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

@NotThreadSafe
public class StartContainerCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(StartContainerCmdIT.class);

    @Test
    public void startContainerWithVolumes() throws Exception {

        // see http://docs.docker.io/use/working_with_volumes/
        Volume volume1 = new Volume("/opt/webapp1");

        Volume volume2 = new Volume("/opt/webapp2");

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withVolumes(volume1, volume2)
                .withCmd("true")
                .withHostConfig(newHostConfig()
                        .withBinds(new Bind("/tmp/webapp1", volume1, ro), new Bind("/tmp/webapp2", volume2)))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getVolumes().keySet(), contains("/opt/webapp1", "/opt/webapp2"));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        dockerRule.getClient().waitContainerCmd(container.getId()).start().awaitStatusCode();

        inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse, mountedVolumes(containsInAnyOrder(volume1, volume2)));

        final List<InspectContainerResponse.Mount> mounts = inspectContainerResponse.getMounts();

        assertThat(mounts, hasSize(2));

        final InspectContainerResponse.Mount mount1 = new InspectContainerResponse.Mount()
                .withRw(false).withMode("ro").withDestination(volume1).withSource("/tmp/webapp1");
        final InspectContainerResponse.Mount mount2 = new InspectContainerResponse.Mount()
                .withRw(true).withMode("rw").withDestination(volume2).withSource("/tmp/webapp2");

        assertThat(mounts, containsInAnyOrder(mount1, mount2));
    }

    @Test
    public void startContainerWithVolumesFrom() throws DockerException {

        Volume volume1 = new Volume("/opt/webapp1");
        Volume volume2 = new Volume("/opt/webapp2");

        String container1Name = UUID.randomUUID().toString();

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(container1Name)
                .withHostConfig(newHostConfig()
                        .withBinds(new Bind("/tmp/webapp1", volume1), new Bind("/tmp/webapp2", volume2)))
                .exec();
        LOG.info("Created container1 {}", container1.toString());

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();
        LOG.info("Started container1 {}", container1.toString());

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();

        assertThat(inspectContainerResponse1, mountedVolumes(containsInAnyOrder(volume1, volume2)));

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withVolumesFrom(new VolumesFrom(container1Name)))
                .exec();
        LOG.info("Created container2 {}", container2.toString());

        dockerRule.getClient().startContainerCmd(container2.getId()).exec();
        LOG.info("Started container2 {}", container2.toString());

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();

        assertThat(inspectContainerResponse2, mountedVolumes(containsInAnyOrder(volume1, volume2)));
    }

    @Test
    public void startContainerWithDns() throws DockerException {

        String aDnsServer = "8.8.8.8";
        String anotherDnsServer = "8.8.4.4";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true")
                .withHostConfig(newHostConfig()
                        .withDns(aDnsServer, anotherDnsServer))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()),
                contains(aDnsServer, anotherDnsServer));
    }

    @Test
    public void startContainerWithDnsSearch() throws DockerException {

        String dnsSearch = "example.com";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true")
                .withHostConfig(newHostConfig()
                        .withDnsSearch(dnsSearch))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDnsSearch()), contains(dnsSearch));
    }

    @Test
    public void startContainerWithPortBindings() throws DockerException {
        int baseport = 20_000 + (getFactoryType().ordinal() * 1000);

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.bindPort(baseport + 22));
        portBindings.bind(tcp23, Binding.bindPort(baseport + 23));
        portBindings.bind(tcp23, Binding.bindPort(baseport + 24));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true")
                .withExposedPorts(tcp22, tcp23)
                .withHostConfig(newHostConfig()
                        .withPortBindings(portBindings)).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()), contains(tcp22, tcp23));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp22)[0],
                is(equalTo(Binding.bindPort(baseport + 22))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[0],
                is(equalTo(Binding.bindPort(baseport + 23))));

        assertThat(inspectContainerResponse.getHostConfig().getPortBindings().getBindings().get(tcp23)[1],
                is(equalTo(Binding.bindPort(baseport + 24))));

    }

    @Test
    public void startContainerWithRandomPortBindings() throws DockerException {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.empty());
        portBindings.bind(tcp23, Binding.empty());

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withExposedPorts(tcp22, tcp23).withHostConfig(newHostConfig()
                        .withPortBindings(portBindings)
                        .withPublishAllPorts(true))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(Arrays.asList(inspectContainerResponse.getConfig().getExposedPorts()), contains(tcp22, tcp23));

        assertThat(inspectContainerResponse.getNetworkSettings().getPorts().getBindings().get(tcp22)[0].getHostPortSpec(),
                is(not(equalTo(String.valueOf(tcp22.getPort())))));

        assertThat(inspectContainerResponse.getNetworkSettings().getPorts().getBindings().get(tcp23)[0].getHostPortSpec(),
                is(not(equalTo(String.valueOf(tcp23.getPort())))));

    }

    @Test(expected = InternalServerErrorException.class)
    public void startContainerWithConflictingPortBindings() throws DockerException {

        ExposedPort tcp22 = ExposedPort.tcp(22);
        ExposedPort tcp23 = ExposedPort.tcp(23);

        Ports portBindings = new Ports();
        portBindings.bind(tcp22, Binding.bindPort(11022));
        portBindings.bind(tcp23, Binding.bindPort(11022));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true")
                .withExposedPorts(tcp22, tcp23).withHostConfig(newHostConfig()
                        .withPortBindings(portBindings))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();
    }

    @Test
    public void startContainerWithLinkingDeprecated() throws DockerException {
        String container1Name = "containerWithLink1" + dockerRule.getKind();
        String container2Name = "containerWithLink2" + dockerRule.getKind();
        dockerRule.ensureContainerRemoved(container1Name);
        dockerRule.ensureContainerRemoved(container2Name);

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .withName(container1Name)
                .exec();

        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

        assertThat(inspectContainerResponse1.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getId(), not(is(emptyString())));
        assertThat(inspectContainerResponse1.getId(), startsWith(container1.getId()));
        assertThat(inspectContainerResponse1.getName(), equalTo("/" + container1Name));
        assertThat(inspectContainerResponse1.getImageId(), not(is(emptyString())));
        assertThat(inspectContainerResponse1.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        if (!inspectContainerResponse1.getState().getRunning()) {
            assertThat(inspectContainerResponse1.getState().getExitCode(), is(equalTo(0)));
        }

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(container2Name).withHostConfig(newHostConfig()
                        .withLinks(new Link(container1Name, container1Name + "Link")))
                .exec();

        LOG.info("Created container2 {}", container2.toString());
        assertThat(container2.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container2.getId()).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();
        LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

        assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getId(), not(is(emptyString())));
        assertThat(inspectContainerResponse2.getHostConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[]{new Link(container1Name,
                container1Name + "Link")}));
        assertThat(inspectContainerResponse2.getId(), startsWith(container2.getId()));
        assertThat(inspectContainerResponse2.getName(), equalTo("/" + container2Name));
        assertThat(inspectContainerResponse2.getImageId(), not(is(emptyString())));
        assertThat(inspectContainerResponse2.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getState().getRunning(), is(true));

    }

    @Test
    public void startContainerWithLinking() throws DockerException {
        String container1Name = "containerWithLinking1" + dockerRule.getKind();
        String container2Name = "containerWithLinking2" + dockerRule.getKind();
        dockerRule.ensureContainerRemoved(container1Name);
        dockerRule.ensureContainerRemoved(container2Name);

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .withName(container1Name)
                .exec();

        LOG.info("Created container1 {}", container1.toString());
        assertThat(container1.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerRule.getClient().inspectContainerCmd(container1.getId())
                .exec();
        LOG.info("Container1 Inspect: {}", inspectContainerResponse1.toString());

        assertThat(inspectContainerResponse1.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getId(), not(is(emptyString())));
        assertThat(inspectContainerResponse1.getId(), startsWith(container1.getId()));
        assertThat(inspectContainerResponse1.getName(), equalTo("/" + container1Name));
        assertThat(inspectContainerResponse1.getImageId(), not(is(emptyString())));
        assertThat(inspectContainerResponse1.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse1.getState().getRunning(), is(true));

        if (!inspectContainerResponse1.getState().getRunning()) {
            assertThat(inspectContainerResponse1.getState().getExitCode(), is(equalTo(0)));
        }

        CreateContainerResponse container2 = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(container2Name).withHostConfig(newHostConfig()
                        .withLinks(new Link(container1Name, container1Name + "Link")))
                .exec();

        LOG.info("Created container2 {}", container2.toString());
        assertThat(container2.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container2.getId()).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container2.getId())
                .exec();
        LOG.info("Container2 Inspect: {}", inspectContainerResponse2.toString());

        assertThat(inspectContainerResponse2.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getId(), not(is(emptyString())));
        assertThat(inspectContainerResponse2.getHostConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getHostConfig().getLinks(), equalTo(new Link[]{new Link(container1Name,
                container1Name + "Link")}));
        assertThat(inspectContainerResponse2.getId(), startsWith(container2.getId()));
        assertThat(inspectContainerResponse2.getName(), equalTo("/" + container2Name));
        assertThat(inspectContainerResponse2.getImageId(), not(is(emptyString())));
        assertThat(inspectContainerResponse2.getState(), is(notNullValue()));
        assertThat(inspectContainerResponse2.getState().getRunning(), is(true));

    }

    @Test
    public void startContainer() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd(new String[]{"top"})
                .exec();

        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getConfig(), is(notNullValue()));
        assertThat(inspectContainerResponse.getId(), not(is(emptyString())));

        assertThat(inspectContainerResponse.getId(), startsWith(container.getId()));

        assertThat(inspectContainerResponse.getImageId(), not(is(emptyString())));
        assertThat(inspectContainerResponse.getState(), is(notNullValue()));

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        if (!inspectContainerResponse.getState().getRunning()) {
            assertThat(inspectContainerResponse.getState().getExitCode(), is(equalTo(0)));
        }
    }

    @Test(expected = NotFoundException.class)
    public void testStartNonExistingContainer() throws DockerException {

        dockerRule.getClient().startContainerCmd("non-existing").exec();
    }

    /**
     * This tests support for --net option for the docker run command: --net="bridge" Set the Network mode for the container 'bridge':
     * creates a new network stack for the container on the docker bridge 'none': no networking for this container 'container:': reuses
     * another container network stack 'host': use the host network stack inside the container. Note: the host mode gives the container full
     * access to local system services such as D-bus and is therefore considered insecure.
     */
    @Test
    public void startContainerWithNetworkMode() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true")
                .withHostConfig(newHostConfig()
                        .withNetworkMode("host"))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getNetworkMode(), is(equalTo("host")));
    }

    @Test
    public void startContainerWithCapAddAndCapDrop() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withCapAdd(NET_ADMIN)
                        .withCapDrop(MKNOD))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapAdd()), contains(NET_ADMIN));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getCapDrop()), contains(MKNOD));
    }

    @Test
    public void startContainerWithDevices() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withDevices(new Device("rwm", "/dev/nulo", "/dev/zero")))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDevices()), contains(new Device("rwm",
                "/dev/nulo", "/dev/zero")));
    }

    @Test
    public void startContainerWithExtraHosts() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withExtraHosts("dockerhost:127.0.0.1"))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getExtraHosts()),
                contains("dockerhost:127.0.0.1"));
    }

    @Test
    public void startContainerWithRestartPolicy() throws DockerException {

        RestartPolicy restartPolicy = RestartPolicy.onFailureRestart(5);

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("sleep", "9999")
                .withHostConfig(newHostConfig()
                        .withRestartPolicy(restartPolicy))
                .exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        assertThat(inspectContainerResponse.getHostConfig().getRestartPolicy(), is(equalTo(restartPolicy)));
    }

    @Test
    public void existingHostConfigIsPreservedByBlankStartCmd() throws DockerException {

        String dnsServer = "8.8.8.8";

        // prepare a container with custom DNS
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withHostConfig(newHostConfig()
                        .withDns(dnsServer))
                .withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        // start container _without_any_customization_ (important!)
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        // The DNS setting survived.
        assertThat(inspectContainerResponse.getHostConfig().getDns(), is(notNullValue()));
        assertThat(Arrays.asList(inspectContainerResponse.getHostConfig().getDns()), contains(dnsServer));
    }
}
