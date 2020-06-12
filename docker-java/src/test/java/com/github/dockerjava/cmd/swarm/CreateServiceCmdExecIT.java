package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.EndpointResolutionMode;
import com.github.dockerjava.api.model.EndpointSpec;
import com.github.dockerjava.api.model.Mount;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.PortConfig;
import com.github.dockerjava.api.model.PortConfigProtocol;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.api.model.TmpfsOptions;
import com.github.dockerjava.junit.PrivateRegistryRule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class CreateServiceCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(CreateServiceCmdExecIT.class);
    private static final String SERVICE_NAME = "theservice";

    @ClassRule
    public static PrivateRegistryRule REGISTRY = new PrivateRegistryRule();

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private AuthConfig authConfig;

    private DockerClient dockerClient;

    @Before
    public final void setUpCreateServiceCmdExecIT() throws Exception {
        authConfig = REGISTRY.getAuthConfig();
        dockerClient = startSwarm();
    }

    @Test
    public void testCreateService() throws DockerException {
        dockerClient.createServiceCmd(new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage(DEFAULT_IMAGE))))
                .exec();

        List<Service> services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        dockerClient.removeServiceCmd(SERVICE_NAME).exec();
    }

    @Test
    public void testCreateServiceWithNetworks() {
        String networkId = dockerClient.createNetworkCmd().withName("networkname")
                .withDriver("overlay")
                .withIpam(new Network.Ipam()
                        .withDriver("default"))
                .exec().getId();
        ServiceSpec spec = new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withForceUpdate(0)
                        .withRuntime("container")
                        .withContainerSpec(new ContainerSpec()
                                .withImage("busybox"))
                )
                .withNetworks(Lists.newArrayList(
                        new NetworkAttachmentConfig()
                                .withTarget(networkId)
                                .withAliases(Lists.<String>newArrayList("alias1", "alias2"))
                ))
                .withLabels(ImmutableMap.of("com.docker.java.usage", "SwarmServiceIT"))
                .withMode(new ServiceModeConfig().withReplicated(
                        new ServiceReplicatedModeOptions()
                                .withReplicas(1)
                )).withEndpointSpec(new EndpointSpec()
                        .withMode(EndpointResolutionMode.VIP)
                        .withPorts(Lists.<PortConfig>newArrayList(new PortConfig()
                                .withPublishMode(PortConfig.PublishMode.host)
                                .withPublishedPort(22)
                                .withProtocol(PortConfigProtocol.TCP)
                        )));

        dockerClient.createServiceCmd(spec).exec();

        List<Service> services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        assertThat(services.get(0).getSpec(), is(spec));

        dockerClient.removeServiceCmd(SERVICE_NAME).exec();
    }

    @Test
    public void testCreateServiceWithTmpfs() {
        Mount tmpMount = new Mount().withTmpfsOptions(new TmpfsOptions().withSizeBytes(600L)).withTarget("/tmp/foo");

        dockerClient.createServiceCmd(new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec().withImage(DEFAULT_IMAGE).withMounts(Collections.singletonList(tmpMount)))))
                .exec();

        List<Service> services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));
        List<Mount> mounts = dockerClient.inspectServiceCmd(SERVICE_NAME).exec().getSpec().getTaskTemplate()
                .getContainerSpec().getMounts();
        assertThat(mounts, hasSize(1));
        assertThat(mounts.get(0), is(tmpMount));
        dockerClient.removeServiceCmd(SERVICE_NAME).exec();
    }

    @Test
    public void testCreateServiceWithValidAuth() throws DockerException {
        dockerClient.createServiceCmd(new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage(DEFAULT_IMAGE))))
                .withAuthConfig(authConfig)
                .exec();

        List<Service> services = dockerClient.listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        dockerClient.removeServiceCmd(SERVICE_NAME).exec();
    }

    @Test
    @Ignore // TODO rework test (does not throw as expected atm)
    public void testCreateServiceWithInvalidAuth() throws DockerException {
        AuthConfig invalidAuthConfig = new AuthConfig()
                .withUsername("testuser")
                .withPassword("testwrongpassword")
                .withEmail("foo@bar.de")
                .withRegistryAddress(authConfig.getRegistryAddress());

        exception.expect(ConflictException.class);

        dockerClient.createServiceCmd(new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage(DEFAULT_IMAGE))))
                .withAuthConfig(invalidAuthConfig)
                .exec();
    }
}
