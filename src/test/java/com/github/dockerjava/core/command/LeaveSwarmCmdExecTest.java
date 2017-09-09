package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.LocalNodeState;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.core.command.swarm.AbstractSwarmDockerClientTest;
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
import static org.hamcrest.Matchers.is;

@Test(groups = "swarm-integration")
public class LeaveSwarmCmdExecTest extends AbstractSwarmDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(LeaveSwarmCmdExecTest.class);

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

    @Test
    public void leaveSwarmAsMaster() throws DockerException {
        SwarmSpec swarmSpec = new SwarmSpec().withName("firstSpec");
        dockerClient.initializeSwarmCmd(swarmSpec)
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();
        LOG.info("Initialized swarm: {}", swarmSpec.toString());

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

    @Test(expectedExceptions = NotAcceptableException.class)
    public void leavingSwarmThrowsWhenNotInSwarm() throws DockerException {
        dockerClient.leaveSwarmCmd().exec();
    }

}
