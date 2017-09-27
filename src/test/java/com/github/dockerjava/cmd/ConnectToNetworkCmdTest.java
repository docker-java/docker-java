package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Network;
import net.jcip.annotations.ThreadSafe;
import org.junit.Test;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * TODO fix parallel.
 */
@ThreadSafe
public class ConnectToNetworkCmdTest extends CmdTest {

    @Test
    public void connectToNetwork() throws InterruptedException {
        assumeNotSwarm("no network in swarm", dockerRule);

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        CreateNetworkResponse network = dockerRule.getClient().createNetworkCmd().withName("testNetwork").exec();

        dockerRule.getClient().connectToNetworkCmd().withNetworkId(network.getId()).withContainerId(container.getId()).exec();

        Network updatedNetwork = dockerRule.getClient().inspectNetworkCmd().withNetworkId(network.getId()).exec();

        assertTrue(updatedNetwork.getContainers().containsKey(container.getId()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertNotNull(inspectContainerResponse.getNetworkSettings().getNetworks().get("testNetwork"));
    }

    @Test
    public void connectToNetworkWithContainerNetwork() throws InterruptedException {
        assumeNotSwarm("no network in swarm", dockerRule);

        final String networkSubnet = "10.100.102.0/24";
        final String networkName = "TestNetwork" + dockerRule.getKind();
        final String CONTAINER_IP = "10.100.102.100";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sleep", "9999")
                .exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        try {
            dockerRule.getClient().removeNetworkCmd(networkName).exec();
        } catch (DockerException ignore) {
        }

        CreateNetworkResponse network = dockerRule.getClient().createNetworkCmd()
                .withName(networkName)
                .withIpam(new Network.Ipam()
                        .withConfig(new Network.Ipam.Config()
                                .withSubnet(networkSubnet)))
                .exec();

        dockerRule.getClient().connectToNetworkCmd()
                .withNetworkId(network.getId())
                .withContainerId(container.getId())
                .withContainerNetwork(new ContainerNetwork()
                        .withAliases("aliasName")
                        .withIpamConfig(new ContainerNetwork.Ipam()
                                .withIpv4Address(CONTAINER_IP)))
                .exec();

        Network updatedNetwork = dockerRule.getClient().inspectNetworkCmd().withNetworkId(network.getId()).exec();

        Network.ContainerNetworkConfig containerNetworkConfig = updatedNetwork.getContainers().get(container.getId());
        assertNotNull(containerNetworkConfig);
        assertThat(containerNetworkConfig.getIpv4Address(), is(CONTAINER_IP + "/24"));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        ContainerNetwork testNetwork = inspectContainerResponse.getNetworkSettings().getNetworks().get(networkName);
        assertNotNull(testNetwork);
        assertThat(testNetwork.getAliases(), hasItem("aliasName"));
        assertThat(testNetwork.getGateway(), is("10.100.102.1"));
        assertThat(testNetwork.getIpAddress(), is(CONTAINER_IP));
    }
}
