package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Network;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

/**
 * @author Kanstantsin Shautsou
 */
public class RemoveNetworkCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(RemoveNetworkCmdIT.class);

    @Test
    public void removeNetwork() throws DockerException {
        assumeNotSwarm("Swarm has no network", dockerRule);

        CreateNetworkResponse network = dockerRule.getClient().createNetworkCmd()
                .withName("test-network")
                .exec();

        LOG.info("Removing network: {}", network.getId());
        dockerRule.getClient().removeNetworkCmd(network.getId()).exec();

        List<Network> networks = dockerRule.getClient().listNetworksCmd().exec();

        Matcher matcher = not(hasItem(hasField("id", startsWith(network.getId()))));
        assertThat(networks, matcher);

    }

    @Test(expected = NotFoundException.class)
    public void removeNonExistingContainer() throws DockerException {
        assumeNotSwarm("Swarm has no network", dockerRule);

        dockerRule.getClient().removeNetworkCmd("non-existing").exec();
    }
}
