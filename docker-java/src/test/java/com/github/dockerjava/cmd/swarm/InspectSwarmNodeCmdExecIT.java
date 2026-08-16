package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SwarmNode;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InspectSwarmNodeCmdExecIT extends SwarmCmdIT {

    @Test
    public void testInspectSwarmNode() {
        DockerClient dockerClient = startSwarm();

        List<SwarmNode> nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(nodes.size(), is(1));

        String nodeId = nodes.get(0).getId();
        SwarmNode node = dockerClient.inspectSwarmNodeCmd(nodeId).exec();

        assertThat(node.getId(), is(nodeId));
    }
}
