package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Network;
import org.junit.Test;

import java.util.List;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static com.github.dockerjava.utils.TestUtils.findNetwork;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class InspectNetworkCmdIT extends CmdIT {

    @Test
    public void inspectNetwork() throws DockerException {
        assumeNotSwarm("no network in swarm", dockerRule);

        List<Network> networks = dockerRule.getClient().listNetworksCmd().exec();

        Network expected = findNetwork(networks, "bridge");

        Network network = dockerRule.getClient().inspectNetworkCmd().withNetworkId(expected.getId()).exec();

        assertThat(network.getName(), equalTo(expected.getName()));
        assertThat(network.getScope(), equalTo(expected.getScope()));
        assertThat(network.getDriver(), equalTo(expected.getDriver()));
        assertThat(network.getIpam().getConfig().get(0).getSubnet(), equalTo(expected.getIpam().getConfig().get(0).getSubnet()));
        assertThat(network.getIpam().getDriver(), equalTo(expected.getIpam().getDriver()));
    }
}
