package com.github.dockerjava.core.command;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.test.serdes.JSONTestHelper;

@Test(groups = "integration")
public class InspectExecCmdImplTest extends AbstractDockerClientTest {
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
    public void inspectExec() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        // Check that file does not exist
        ExecCreateCmdResponse checkFileExec1 = dockerClient.execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("test", "-e", "/marker").exec();
        LOG.info("Created exec {}", checkFileExec1.toString());
        assertThat(checkFileExec1.getId(), not(isEmptyString()));
        dockerClient.execStartCmd(checkFileExec1.getId()).withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
        InspectExecResponse first = dockerClient.inspectExecCmd(checkFileExec1.getId()).exec();
        assertThat(first.isRunning(), is(false));
        assertThat(first.getExitCode(), is(1));

        // Create the file
        ExecCreateCmdResponse touchFileExec = dockerClient.execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("touch", "/marker").exec();
        LOG.info("Created exec {}", touchFileExec.toString());
        assertThat(touchFileExec.getId(), not(isEmptyString()));
        dockerClient.execStartCmd(touchFileExec.getId()).withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
        InspectExecResponse second = dockerClient.inspectExecCmd(touchFileExec.getId()).exec();
        assertThat(second.isRunning(), is(false));
        assertThat(second.getExitCode(), is(0));

        // Check that file does exist now
        ExecCreateCmdResponse checkFileExec2 = dockerClient.execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("test", "-e", "/marker").exec();
        LOG.info("Created exec {}", checkFileExec2.toString());
        assertThat(checkFileExec2.getId(), not(isEmptyString()));
        dockerClient.execStartCmd(checkFileExec2.getId()).withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
        InspectExecResponse third = dockerClient.inspectExecCmd(checkFileExec2.getId()).exec();
        assertThat(third.isRunning(), is(false));
        assertThat(third.getExitCode(), is(0));

        // Get container info and check its roundtrip to ensure the consistency
        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());
        JSONTestHelper.testRoundTrip(containerInfo);
    }

    @Test(groups = "ignoreInCircleCi")
    public void inspectExecNetworkSettings() throws IOException {
        final RemoteApiVersion apiVersion = getVersion(dockerClient);

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse exec = dockerClient.execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("/bin/bash").exec();
        LOG.info("Created exec {}", exec.toString());
        assertThat(exec.getId(), not(isEmptyString()));

        InspectExecResponse inspectExecResponse = dockerClient.inspectExecCmd(exec.getId()).exec();

        if (apiVersion.isGreaterOrEqual(RemoteApiVersion.VERSION_1_22)) {
            assertThat(inspectExecResponse.getExitCode(), is(nullValue()));
            assertThat(inspectExecResponse.getCanRemove(), is(false));
            assertThat(inspectExecResponse.getContainerID(), is(container.getId()));
        } else {
            assertThat(inspectExecResponse.getExitCode(), is(0));
            assertNotNull(inspectExecResponse.getContainer().getNetworkSettings().getNetworks().get("bridge"));
        }

        assertThat(inspectExecResponse.isOpenStdin(), is(false));
        assertThat(inspectExecResponse.isOpenStdout(), is(true));
        assertThat(inspectExecResponse.isRunning(), is(false));

        final InspectExecResponse.Container inspectContainer = inspectExecResponse.getContainer();
        if (apiVersion.isGreaterOrEqual(VERSION_1_22)) {
            assertThat(inspectContainer, nullValue());
        } else {
            assertThat(inspectContainer, notNullValue());
            assertNotNull(inspectContainer.getNetworkSettings().getNetworks().get("bridge"));
        }
    }
}
