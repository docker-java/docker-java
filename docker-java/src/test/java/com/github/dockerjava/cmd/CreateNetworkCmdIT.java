package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Network;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_21;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_25;
import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeThat;

@NotThreadSafe
public class CreateNetworkCmdIT extends CmdIT {

    @Test
    public void createNetwork() throws DockerException {
        assumeNotSwarm("no network in swarm", dockerRule);

        String networkName = "createNetwork" + dockerRule.getKind();

        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd().withName(networkName).exec();

        assertNotNull(createNetworkResponse.getId());

        Network network = dockerRule.getClient().inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertThat(network.getName(), is(networkName));
        assertThat(network.getDriver(), is("bridge"));
    }

    @Test
    public void createNetworkWithIpamConfig() throws DockerException {
        assumeNotSwarm("no network in swarm", dockerRule);

        String networkName = "networkIpam" + dockerRule.getKind();
        String subnet = "10.67." + (79 + getFactoryType().ordinal()) + ".0/24";

        Network.Ipam ipam = new Network.Ipam().withConfig(new Network.Ipam.Config().withSubnet(subnet));
        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd().withName(networkName).withIpam(ipam).exec();

        assertNotNull(createNetworkResponse.getId());

        Network network = dockerRule.getClient().inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertEquals(network.getName(), networkName);
        assertEquals("bridge", network.getDriver());
        assertEquals(subnet, network.getIpam().getConfig().iterator().next().getSubnet());
    }

    @Test
    public void createAttachableNetwork() throws DockerException {
        assumeThat("API version should be > 1.24", dockerRule, isGreaterOrEqual(VERSION_1_25));

        String networkName = "createAttachableNetwork" + dockerRule.getKind();
        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd()
                .withName(networkName)
                .withAttachable(true)
                .exec();
        assertNotNull(createNetworkResponse.getId());
        Network network = dockerRule.getClient().inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertThat(network, notNullValue());
        assertTrue(network.isAttachable());
    }

    @Test
    public void createNetworkWithLabel() throws DockerException {
        assumeNotSwarm("no network in swarm?", dockerRule);
        assumeThat("API version should be >= 1.21", dockerRule, isGreaterOrEqual(VERSION_1_21));

        String networkName = "createNetworkWithLabel" + dockerRule.getKind();
        Map<String, String> labels = new HashMap<>();
        labels.put("com.example.usage" + dockerRule.getKind(), "test");
        CreateNetworkResponse createNetworkResponse = dockerRule.getClient().createNetworkCmd().withName(networkName).withLabels(labels).exec();
        assertNotNull(createNetworkResponse.getId());
        Network network = dockerRule.getClient().inspectNetworkCmd().withNetworkId(createNetworkResponse.getId()).exec();
        assertEquals(network.getLabels(), labels);
    }
}
