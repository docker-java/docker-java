package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SwarmNode;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ListSwarmNodesCmdExecIT extends SwarmCmdIT {
    @Test
    public void testListSwarmNodes() throws Exception {
        DockerClient dockerClient = startSwarm();

        List<SwarmNode> nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(nodes.size(), is(1));
    }

    @Test
    public void testListSwarmNodesWithIdFilter() throws Exception {
        DockerClient dockerClient = startSwarm();

        List<SwarmNode> nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(nodes.size(), is(1));

        String nodeId = nodes.get(0).getId();
        List<SwarmNode> nodesWithId = dockerClient.listSwarmNodesCmd()
            .withIdFilter(Collections.singletonList(nodeId))
            .exec();
        assertThat(nodesWithId.size(), is(1));

        List<SwarmNode> nodesWithNonexistentId = dockerClient.listSwarmNodesCmd()
            .withIdFilter(Collections.singletonList("__nonexistent__"))
            .exec();
        assertThat(nodesWithNonexistentId.size(), is(0));
    }

    @Test
    public void testListSwarmNodesWithNameFilter() throws Exception {
        DockerClient dockerClient = startSwarm();

        List<SwarmNode> nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(nodes.size(), is(1));

        String nodeName = nodes.get(0).getSpec().getName();
        List<SwarmNode> nodesWithFirstNodesName = dockerClient.listSwarmNodesCmd()
            .withNameFilter(Collections.singletonList(nodeName))
            .exec();
        assertThat(nodesWithFirstNodesName.size(), is(1));

        List<SwarmNode> nodesWithNonexistentName = dockerClient.listSwarmNodesCmd()
            .withNameFilter(Collections.singletonList("__nonexistent__"))
            .exec();
        assertThat(nodesWithNonexistentName.size(), is(0));
    }

    @Test
    public void testListSwarmNodesWithMembershipFilter() throws Exception {
        DockerClient dockerClient = startSwarm();

        List<SwarmNode> nodesWithAcceptedMembership = dockerClient.listSwarmNodesCmd()
            .withMembershipFilter(Collections.singletonList("accepted"))
            .exec();
        assertThat(nodesWithAcceptedMembership.size(), is(1));

        List<SwarmNode> nodesWithPendingMembership = dockerClient.listSwarmNodesCmd()
            .withMembershipFilter(Collections.singletonList("pending"))
            .exec();
        assertThat(nodesWithPendingMembership.size(), is(0));
    }

    @Test
    public void testListSwarmNodesWithRoleFilter() throws Exception {
        DockerClient dockerClient = startSwarm();

        List<SwarmNode> nodesWithManagerRole = dockerClient.listSwarmNodesCmd()
            .withRoleFilter(Collections.singletonList("manager"))
            .exec();
        assertThat(nodesWithManagerRole.size(), is(1));

        List<SwarmNode> nodesWithWorkerRole = dockerClient.listSwarmNodesCmd()
            .withRoleFilter(Collections.singletonList("worker"))
            .exec();
        assertThat(nodesWithWorkerRole.size(), is(0));
    }
}
