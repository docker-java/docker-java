package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Consistency;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.EndpointResolutionMode;
import com.github.dockerjava.api.model.EndpointSpec;
import com.github.dockerjava.api.model.Mount;
import com.github.dockerjava.api.model.MountType;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.PortConfig;
import com.github.dockerjava.api.model.PortConfigProtocol;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateServiceCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(CreateServiceCmdExecIT.class);
    private static final String SERVICE_NAME = "theservice";

    @Before
    public void setUp() throws Exception {
        dockerRule.getClient().initializeSwarmCmd(new SwarmSpec())
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();
    }

    @Test
    public void testCreateService() throws DockerException {
        dockerRule.getClient().createServiceCmd(new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec()
                                .withImage(DEFAULT_IMAGE))))
                .exec();

        List<Service> services = dockerRule.getClient().listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));
    }

    @Test
    public void testCreateServiceWithNetworks() {
        String networkId = dockerRule.getClient().createNetworkCmd().withName("networkname")
                .withDriver("overlay")
                .withIpam(new Network.Ipam()
                        .withDriver("default"))
                .exec().getId();
        ServiceSpec spec = new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withForceUpdate(0)
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

        dockerRule.getClient().createServiceCmd(spec).exec();

        List<Service> services = dockerRule.getClient().listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));

        assertThat(services.get(0).getSpec(), is(spec));
    }


    @Test
    public void testCreateServiceWithConsistency() {
        final Mount mount = new Mount().withTarget("/tmp/foo")
                .withType(MountType.BIND)
                .withConsistency(Consistency.DELEGATED);
        final ContainerSpec containerSpec = new ContainerSpec()
                .withImage("busybox")
                .withMounts(Collections.singletonList(mount));
        final TaskSpec taskSpec = new TaskSpec()
                .withForceUpdate(0)
                .withContainerSpec(containerSpec);
        final ServiceModeConfig modeConfig = new ServiceModeConfig()
                .withReplicated(new ServiceReplicatedModeOptions().withReplicas(1));
        final ServiceSpec spec = new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(taskSpec)
                .withLabels(ImmutableMap.of("com.docker.java.usage", "SwarmServiceIT"))
                .withMode(modeConfig)
                .withEndpointSpec(new EndpointSpec()
                        .withMode(EndpointResolutionMode.VIP)
                        .withPorts(Lists.newArrayList(new PortConfig()
                                .withPublishMode(PortConfig.PublishMode.host)
                                .withPublishedPort(22)
                                .withProtocol(PortConfigProtocol.TCP)
                        )));

        dockerRule.getClient().createServiceCmd(spec).exec();

        List<Service> services = dockerRule.getClient().listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));
        ServiceSpec serviceSpec = services.get(0).getSpec();
        assertNotNull(serviceSpec);
        // The `Consistency` field is not filled in the response
        assertEquals(SERVICE_NAME, serviceSpec.getName());
        List<Mount> mounts = serviceSpec.getTaskTemplate().getContainerSpec().getMounts();
        assertThat(mounts, hasSize(1));
        Mount mount1 = mounts.get(0);
        assertEquals("/tmp/foo", mount1.getTarget());
    }

    @After
    public void tearDown() throws Exception {
        dockerRule.getClient().removeServiceCmd(SERVICE_NAME).exec();
    }
}
