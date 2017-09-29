package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotAcceptableException;
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

public class InitializeSwarmCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(InitializeSwarmCmdExecIT.class);

    @Test
    public void initializeSwarm() throws DockerException {
        SwarmSpec swarmSpec = new SwarmSpec()
                .withName("swarm")
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

        dockerRule.getClient().initializeSwarmCmd(swarmSpec)
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();
        LOG.info("Initialized swarm: {}", swarmSpec.toString());

        Swarm swarm = dockerRule.getClient().inspectSwarmCmd().exec();
        LOG.info("Inspected swarm: {}", swarm.toString());
        assertThat(swarm.getSpec(), is(equalTo(swarmSpec)));
    }

    @Test(expected = NotAcceptableException.class)
    public void initializingSwarmThrowsWhenAlreadyInSwarm() throws DockerException {
        SwarmSpec swarmSpec = new SwarmSpec()
                .withName("swarm");

        dockerRule.getClient().initializeSwarmCmd(swarmSpec)
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();
        LOG.info("Initialized swarm: {}", swarmSpec.toString());

        // Initializing a swarm if already in swarm mode should fail
        dockerRule.getClient().initializeSwarmCmd(swarmSpec)
                .withListenAddr("127.0.0.1")
                .exec();
    }

}
