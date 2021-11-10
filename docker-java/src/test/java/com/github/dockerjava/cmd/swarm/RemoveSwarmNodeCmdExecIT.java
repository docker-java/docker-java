package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.api.model.SwarmNode;
import com.github.dockerjava.api.model.SwarmNodeRole;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Remove swarm node cmd test
 *
 * @author 訾明华
 * @since 2021-11-10
 */
public class RemoveSwarmNodeCmdExecIT extends SwarmCmdIT {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveSwarmNodeCmdExecIT.class);

    @Test
    public void testUpdateSwarmNode() throws Exception {
        DockerClient dockerClient = startSwarm();
        Swarm swarm = dockerClient.inspectSwarmCmd().exec();

        DockerClient docker2 = startDockerInDocker();
        docker2.joinSwarmCmd()
               .withRemoteAddrs(Lists.newArrayList("docker1"))
               .withJoinToken(swarm.getJoinTokens().getWorker())
               .exec();
        LOGGER.info("docker2 joined docker's swarm");

        List<SwarmNode> nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(2, is(nodes.size()));
        Optional<SwarmNode> firstWorkNode = nodes.stream().filter(node -> node.getSpec().getRole() == SwarmNodeRole.WORKER)
                                                 .findFirst();
        dockerClient.removeSwarmNodeCmd(firstWorkNode.get().getId())
                    .withForce(true)
                    .exec();
        nodes = dockerClient.listSwarmNodesCmd().exec();
        assertThat(nodes.size(), is(1));
    }
}
