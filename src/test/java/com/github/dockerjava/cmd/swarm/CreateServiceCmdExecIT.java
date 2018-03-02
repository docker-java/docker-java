package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.exception.DockerException;
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
import com.github.dockerjava.api.model.TmpfsOptions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class CreateServiceCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(CreateServiceCmdExecIT.class);
    private static final String SERVICE_NAME = "theservice";

    @Test
    public void testCreateService() throws DockerException {
        dockerRule.getClient().initializeSwarmCmd(new SwarmSpec())
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();

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

        dockerRule.getClient().removeServiceCmd(SERVICE_NAME).exec();
    }

    @Test
    public void testCreateServiceWithNetworks() {
        dockerRule.getClient().initializeSwarmCmd(new SwarmSpec())
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();

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

        dockerRule.getClient().removeServiceCmd(SERVICE_NAME).exec();
    }

    @Test
    public void testCreateServiceWithTmpfs() {
        dockerRule.getClient().initializeSwarmCmd(new SwarmSpec())
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();
        Mount tmpMount = new Mount().withTmpfsOptions(new TmpfsOptions().withSizeBytes(600L)).withTarget("/tmp/foo");

        dockerRule.getClient().createServiceCmd(new ServiceSpec()
                .withName(SERVICE_NAME)
                .withTaskTemplate(new TaskSpec()
                        .withContainerSpec(new ContainerSpec().withImage(DEFAULT_IMAGE).withMounts(Collections.singletonList(tmpMount)))))
                .exec();

        List<Service> services = dockerRule.getClient().listServicesCmd()
                .withNameFilter(Lists.newArrayList(SERVICE_NAME))
                .exec();

        assertThat(services, hasSize(1));
        List<Mount> mounts = dockerRule.getClient().inspectServiceCmd(SERVICE_NAME).exec().getSpec().getTaskTemplate()
                .getContainerSpec().getMounts();
        assertThat(mounts, hasSize(1));
        assertThat(mounts.get(0), is(tmpMount));
        dockerRule.getClient().removeServiceCmd(SERVICE_NAME).exec();
    }
}
