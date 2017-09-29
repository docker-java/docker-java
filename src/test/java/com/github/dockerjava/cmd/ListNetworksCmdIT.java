package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.utils.TestUtils;
import org.junit.Test;

import java.util.List;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ListNetworksCmdIT extends CmdIT {

    @Test
    public void listNetworks() throws DockerException {
        assumeNotSwarm("Swarm has no network", dockerRule);

        List<Network> networks = dockerRule.getClient().listNetworksCmd().exec();

        Network network = TestUtils.findNetwork(networks, "bridge");

        assertThat(network.getName(), equalTo("bridge"));
        assertThat(network.getScope(), equalTo("local"));
        assertThat(network.getDriver(), equalTo("bridge"));
        assertThat(network.getIpam().getDriver(), equalTo("default"));
    }

}
