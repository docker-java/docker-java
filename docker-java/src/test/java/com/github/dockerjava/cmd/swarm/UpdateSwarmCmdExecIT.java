package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.api.model.SwarmCAConfig;
import com.github.dockerjava.api.model.SwarmDispatcherConfig;
import com.github.dockerjava.api.model.SwarmOrchestration;
import com.github.dockerjava.api.model.SwarmRaftConfig;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskDefaults;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class UpdateSwarmCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(UpdateSwarmCmdExecIT.class);

    @Test
    public void updateSwarm() throws DockerException {
        DockerClient dockerClient = startSwarm();

        SwarmSpec newSpec = new SwarmSpec()
                .withName("default")
                .withDispatcher(new SwarmDispatcherConfig()
                        .withHeartbeatPeriod(10000000L)
                ).withOrchestration(new SwarmOrchestration()
                        .withTaskHistoryRententionLimit(100)
                ).withCaConfig(new SwarmCAConfig()
                        .withNodeCertExpiry(60 * 60 * 1000000000L /*ns */))
                .withRaft(new SwarmRaftConfig()
                        .withElectionTick(8)
                        .withSnapshotInterval(20000)
                        .withHeartbeatTick(5)
                        .withLogEntriesForSlowFollowers(200)
                ).withTaskDefaults(new TaskDefaults());

        Swarm swarm = dockerClient.inspectSwarmCmd().exec();
        LOG.info("Inspected swarm: {}", swarm.toString());
        assertThat(swarm.getSpec(), is(not(equalTo(newSpec))));

        dockerClient.updateSwarmCmd(newSpec)
                .withVersion(swarm.getVersion().getIndex())
                .exec();
        LOG.info("Updated swarm: {}", newSpec.toString());

        swarm = dockerClient.inspectSwarmCmd().exec();
        LOG.info("Inspected swarm: {}", swarm.toString());
        assertThat(swarm.getSpec(), is(equalTo(newSpec)));
    }

    @Test(expected = DockerException.class)
    public void updatingSwarmThrowsWhenNotInSwarm() throws Exception {
        DockerClient dockerClient = startDockerInDocker();
        dockerClient.updateSwarmCmd(new SwarmSpec())
                .withVersion(1L)
                .exec();
    }

}
