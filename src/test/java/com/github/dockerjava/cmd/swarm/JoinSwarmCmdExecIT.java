package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.LocalNodeState;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.api.model.SwarmJoinTokens;
import com.github.dockerjava.api.model.SwarmSpec;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class JoinSwarmCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(JoinSwarmCmdExecIT.class);

    private SwarmJoinTokens initSwarmOnDocker(DockerClient docker) {
        SwarmSpec swarmSpec = new SwarmSpec();
        docker.initializeSwarmCmd(swarmSpec)
                .exec();
        LOG.info("Initialized swarm docker1: {}", swarmSpec.toString());

        Swarm swarm = docker.inspectSwarmCmd().exec();
        LOG.info("Inspected swarm on docker1: {}", swarm.toString());
        return swarm.getJoinTokens();
    }

    @Test
    public void joinSwarmAsWorker() throws DockerException {
        DockerClient docker1 = startDockerInDocker();
        DockerClient docker2 = startDockerInDocker();

        SwarmJoinTokens tokens = initSwarmOnDocker(docker1);

        docker2.joinSwarmCmd()
                .withRemoteAddrs(Lists.newArrayList("docker1"))
                .withJoinToken(tokens.getWorker())
                .exec();
        LOG.info("docker2 joined docker1's swarm");

        Info info = docker2.infoCmd().exec();
        LOG.info("Inspected docker2: {}", info.toString());
        assertThat(info.getSwarm().getLocalNodeState(), is(equalTo(LocalNodeState.ACTIVE)));
    }

    @Test
    public void joinSwarmAsManager() throws DockerException, InterruptedException {
        DockerClient docker1 = startDockerInDocker();
        DockerClient docker2 = startDockerInDocker();

        SwarmJoinTokens tokens = initSwarmOnDocker(docker1);

        docker2.joinSwarmCmd()
                .withRemoteAddrs(Lists.newArrayList("docker1"))
                .withJoinToken(tokens.getManager())
                .exec();
        LOG.info("docker2 joined docker1's swarm");

        Info info = docker2.infoCmd().exec();
        LOG.info("Inspected docker2: {}", info.toString());
        assertThat(info.getSwarm().getLocalNodeState(), is(equalTo(LocalNodeState.ACTIVE)));
    }

    @Test(expected = NotAcceptableException.class)
    public void joinSwarmIfAlreadyInSwarm() {
        DockerClient docker1 = startDockerInDocker();
        DockerClient docker2 = startDockerInDocker();

        SwarmJoinTokens tokens = initSwarmOnDocker(docker1);

        initSwarmOnDocker(docker2);

        docker2.joinSwarmCmd()
                .withRemoteAddrs(Lists.newArrayList("docker1"))
                .withJoinToken(tokens.getWorker())
                .exec();
    }

}
