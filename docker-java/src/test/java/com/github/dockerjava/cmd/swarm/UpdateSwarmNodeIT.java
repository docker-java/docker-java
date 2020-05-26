package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.api.model.SwarmNodeAvailability;
import com.github.dockerjava.api.model.SwarmNodeSpec;
import com.github.dockerjava.api.model.SwarmNodeState;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UpdateSwarmNodeIT extends SwarmCmdIT {
    @Test
    public void testUpdateSwarmNode() throws Exception {
        DockerClient dockerClient = startSwarm();
        List<SwarmNode> nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(1, is(nodes.size()));
        SwarmNode node = nodes.get(0);
        assertThat(SwarmNodeState.READY, is(node.getStatus().getState()));
        //update the node availability
        SwarmNodeSpec nodeSpec = node.getSpec().withAvailability(SwarmNodeAvailability.PAUSE);
        dockerClient.updateSwarmNodeCmd().withSwarmNodeId(node.getId()).withVersion(node.getVersion().getIndex())
                .withSwarmNodeSpec(nodeSpec).exec();
        nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(nodes.size(), is(1));
        assertThat(nodes.get(0).getSpec().getAvailability(), is(SwarmNodeAvailability.PAUSE));
    }
}
