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
import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * TODO fix parallel.
 */
@ThreadSafe
public class ConnectToNetworkCmdIT extends CmdIT {

    @Test
    public void connectToNetwork() throws InterruptedException {
        assumeNotSwarm("no network in swarm", dockerRule);
        String networkName = "connectToNetwork" + dockerRule.getKind();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("sleep", "9999").exec();
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        CreateNetworkResponse network = dockerRule.getClient().createNetworkCmd().withName(networkName).exec();

        dockerRule.getClient().connectToNetworkCmd().withNetworkId(network.getId()).withContainerId(container.getId()).exec();

        Network updatedNetwork = dockerRule.getClient().inspectNetworkCmd().withNetworkId(network.getId()).exec();

        assertTrue(updatedNetwork.getContainers().containsKey(container.getId()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertNotNull(inspectContainerResponse.getNetworkSettings().getNetworks().get(networkName));
    }

    @Test
    public void connectToNetworkWithContainerNetwork() throws InterruptedException {
        assumeNotSwarm("no network in swarm", dockerRule);

        final String subnetPrefix = getFactoryType().getSubnetPrefix() + "100";
        final String networkName = "ContainerWithNetwork" + dockerRule.getKind();
        final String containerIp = subnetPrefix + ".100";

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
                    .withSubnet(subnetPrefix + ".0/24")))
            .exec();

        dockerRule.getClient().connectToNetworkCmd()
            .withNetworkId(network.getId())
            .withContainerId(container.getId())
            .withContainerNetwork(new ContainerNetwork()
                .withAliases("aliasName" + dockerRule.getKind())
                .withIpamConfig(new ContainerNetwork.Ipam()
                    .withIpv4Address(containerIp)))
            .exec();

        Network updatedNetwork = dockerRule.getClient().inspectNetworkCmd().withNetworkId(network.getId()).exec();

        Network.ContainerNetworkConfig containerNetworkConfig = updatedNetwork.getContainers().get(container.getId());
        assertNotNull(containerNetworkConfig);
        assertThat(containerNetworkConfig.getIpv4Address(), is(containerIp + "/24"));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        ContainerNetwork testNetwork = inspectContainerResponse.getNetworkSettings().getNetworks().get(networkName);
        assertNotNull(testNetwork);
        assertThat(testNetwork.getAliases(), hasItem("aliasName" + dockerRule.getKind()));
        assertThat(testNetwork.getGateway(), is(subnetPrefix + ".1"));
        assertThat(testNetwork.getIpAddress(), is(containerIp));
    }
}
