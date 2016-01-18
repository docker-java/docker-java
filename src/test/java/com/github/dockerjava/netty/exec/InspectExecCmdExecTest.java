package com.github.dockerjava.netty.exec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateExecCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.core.command.StartExecResultCallback;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import com.github.dockerjava.test.serdes.JSONTestHelper;

@Test(groups = "integration")
public class InspectExecCmdExecTest extends AbstractNettyDockerClientTest {

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

    @Test(groups = "ignoreInCircleCi")
    public void inspectExecTest() throws IOException {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        CreateExecCmdResponse touchFileCmdCreateResponse = dockerClient.createExecCmd(container.getId())
                .withAttachStdout(true).withAttachStderr(true).withCmd("touch", "/marker").withTty(true).exec();
        LOG.info("Created exec {}", touchFileCmdCreateResponse.toString());
        assertThat(touchFileCmdCreateResponse.getId(), not(isEmptyString()));
        CreateExecCmdResponse checkFileCmdCreateResponse = dockerClient.createExecCmd(container.getId())
                .withAttachStdout(true).withAttachStderr(true).withCmd("test", "-e", "/marker").exec();
        LOG.info("Created exec {}", checkFileCmdCreateResponse.toString());
        assertThat(checkFileCmdCreateResponse.getId(), not(isEmptyString()));

        // Check that file does not exist
        dockerClient.startExecCmd(container.getId()).withDetach(false).withTty(true)
                .withExecId(checkFileCmdCreateResponse.getId())
                .exec(new StartExecResultCallback(System.out, System.err));

        InspectExecResponse first = dockerClient.inspectExecCmd(checkFileCmdCreateResponse.getId()).exec();
        assertEquals(first.isRunning(), new Boolean(false));
        assertThat(first.getExitCode(), is(1));

        // Create the file
        dockerClient.startExecCmd(container.getId()).withDetach(false).withTty(true)
                .withExecId(touchFileCmdCreateResponse.getId())
                .exec(new StartExecResultCallback(System.out, System.err));

        InspectExecResponse second = dockerClient.inspectExecCmd(touchFileCmdCreateResponse.getId()).exec();
        assertEquals(first.isRunning(), new Boolean(false));
        assertThat(second.getExitCode(), is(0));

        // Check that file does exist now
        dockerClient.startExecCmd(container.getId()).withExecId(checkFileCmdCreateResponse.getId())
                .exec(new StartExecResultCallback(System.out, System.err));

        InspectExecResponse third = dockerClient.inspectExecCmd(checkFileCmdCreateResponse.getId()).exec();
        assertThat(third.getExitCode(), is(0));

        // Get container info and check its roundtrip to ensure the consistency
        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());
        JSONTestHelper.testRoundTrip(containerInfo);
    }

    @Test(groups = "ignoreInCircleCi")
    public void inspectExecNetworkSettings() throws IOException {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        CreateExecCmdResponse exec = dockerClient.createExecCmd(container.getId())
                .withAttachStdout(true).withAttachStderr(true).withCmd("/bin/bash").exec();
        LOG.info("Created exec {}", exec.toString());
        assertThat(exec.getId(), not(isEmptyString()));

        InspectExecResponse inspectExecResponse = dockerClient.inspectExecCmd(exec.getId()).exec();
        assertThat(inspectExecResponse.getExitCode(), is(0));

        assertNotNull(inspectExecResponse.getContainer().getNetworkSettings().getNetworks().get("bridge"));
    }
}
