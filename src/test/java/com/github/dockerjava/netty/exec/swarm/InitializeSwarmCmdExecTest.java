package com.github.dockerjava.netty.exec.swarm;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.api.model.SwarmCAConfig;
import com.github.dockerjava.api.model.SwarmDispatcherConfig;
import com.github.dockerjava.api.model.SwarmOrchestration;
import com.github.dockerjava.api.model.SwarmRaftConfig;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskDefaults;
import com.github.dockerjava.netty.AbstractNettySwarmDockerClientTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Test(groups = "swarm-integration")
public class InitializeSwarmCmdExecTest extends AbstractNettySwarmDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(InitializeSwarmCmdExecTest.class);

    @BeforeTest
    public void beforeTest() throws Exception {
        super.beforeTest();
    }

    @AfterTest
    public void afterTest() {
        super.afterTest();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        super.afterMethod(result);
    }

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

        dockerClient.initializeSwarmCmd(swarmSpec)
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();
        LOG.info("Initialized swarm: {}", swarmSpec.toString());

        Swarm swarm = dockerClient.inspectSwarmCmd().exec();
        LOG.info("Inspected swarm: {}", swarm.toString());
        assertThat(swarm.getSpec(), is(equalTo(swarmSpec)));
    }

    @Test(expectedExceptions = NotAcceptableException.class)
    public void initializingSwarmThrowsWhenAlreadyInSwarm() throws DockerException {
        SwarmSpec swarmSpec = new SwarmSpec()
                .withName("swarm");

        dockerClient.initializeSwarmCmd(swarmSpec)
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();
        LOG.info("Initialized swarm: {}", swarmSpec.toString());

        // Initializing a swarm if already in swarm mode should fail
        dockerClient.initializeSwarmCmd(swarmSpec)
                .withListenAddr("127.0.0.1")
                .exec();
    }

}
