package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceRestartCondition;
import com.github.dockerjava.api.model.ServiceRestartPolicy;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.utils.TestUtils.findNetwork;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InspectNetworkCmdExecIT extends SwarmCmdIT {

    private DockerClient dockerClient;

    private String SERVICE_NAME = "servicename";

    @Before
    public final void setUpCreateServiceCmdExecIT() {
        dockerClient = startSwarm();
        createServiceInNetwork();
    }

    @Test
    public void inspectNetworkIngressNonVerbose() throws DockerException {

        List<Network> networks = dockerClient.listNetworksCmd().exec();

        Network expected = findNetwork(networks, "ingress");

        Network network = dockerClient.inspectNetworkCmd()
                .withNetworkId(expected.getId())
                .exec();

        assertThat(network.getName(), equalTo(expected.getName()));
        assertThat(network.getScope(), equalTo(expected.getScope()));
        assertThat(network.getDriver(), equalTo(expected.getDriver()));
        assertThat(network.getIpam().getConfig().get(0).getSubnet(), equalTo(expected.getIpam().getConfig().get(0).getSubnet()));
        assertThat(network.getIpam().getDriver(), equalTo(expected.getIpam().getDriver()));
        assertThat(network.getServices(), nullValue());
    }

    @Test
    public void inspectNetworkIngressVerbose() throws DockerException {

        List<Network> networks = dockerClient.listNetworksCmd().exec();

        Network expected = findNetwork(networks, "networkname");

        Network network = dockerClient.inspectNetworkCmd()
                .withVerbose(true)
                .withNetworkId(expected.getId())
                .exec();

        assertThat(network.getName(), equalTo(expected.getName()));
        assertThat(network.getScope(), equalTo(expected.getScope()));
        assertThat(network.getDriver(), equalTo(expected.getDriver()));
        assertThat(network.getServices(), notNullValue());

        Map<String, Network.NetworkService> networkServices = network.getServices();
        Network.NetworkService firstService = networkServices.get(networkServices.keySet().iterator().next());
        assertThat(firstService.getTasks(), notNullValue());
        assertThat(firstService.getTasks()[0].getName(), startsWith(SERVICE_NAME));

    }

    private void createServiceInNetwork() {
        List<Network> networks = dockerClient.listNetworksCmd().exec();

        Network n = null;
        try {
            n = findNetwork(networks, "networkname");
        } catch (AssertionError a) {

        }

        if (n != null) {
            return;
        }

        String networkId = dockerClient.createNetworkCmd().withName("networkname").withDriver("overlay")
                .withIpam(new Network.Ipam().withDriver("default")).exec().getId();

        String snippet = "60s";
        TaskSpec taskSpec = new TaskSpec().withContainerSpec(
                        new ContainerSpec().withImage("busybox").withCommand(Arrays.asList("sleep", snippet)))
                .withRestartPolicy(new ServiceRestartPolicy().withCondition(ServiceRestartCondition.NONE));
        ServiceSpec serviceSpec = new ServiceSpec()
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)))
                .withTaskTemplate(taskSpec)
                .withNetworks(Lists.newArrayList(
                        new NetworkAttachmentConfig()
                                .withTarget(networkId)
                ))
                .withName(SERVICE_NAME);
        String serviceId = dockerClient.createServiceCmd(serviceSpec).exec().getId();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
