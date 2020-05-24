package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.LocalNodeState;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LeaveSwarmCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(LeaveSwarmCmdExecIT.class);

    @Test
    public void leaveSwarmAsMaster() throws DockerException {
        DockerClient dockerClient = startSwarm();

        Info info = dockerClient.infoCmd().exec();
        LOG.info("Inspected docker: {}", info.toString());

        assertThat(info.getSwarm().getLocalNodeState(), is(LocalNodeState.ACTIVE));

        dockerClient.leaveSwarmCmd()
                .withForceEnabled(true)
                .exec();
        LOG.info("Left swarm");

        info = dockerClient.infoCmd().exec();
        LOG.info("Inspected docker: {}", info.toString());

        assertThat(info.getSwarm().getLocalNodeState(), is(LocalNodeState.INACTIVE));

    }

    @Test(expected = DockerException.class)
    public void leavingSwarmThrowsWhenNotInSwarm() throws Exception {
        DockerClient dockerClient = startDockerInDocker();
        dockerClient.leaveSwarmCmd().exec();
    }

}
