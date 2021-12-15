package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SecureRandom;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InspectExecCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(InspectExecCmdIT.class);

    @Test
    public void inspectExec() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        // Check that file does not exist
        ExecCreateCmdResponse checkFileExec1 = dockerRule.getClient().execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("test", "-e", "/marker").exec();
        LOG.info("Created exec {}", checkFileExec1.toString());
        assertThat(checkFileExec1.getId(), not(is(emptyString())));
        dockerRule.getClient().execStartCmd(checkFileExec1.getId()).withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
        InspectExecResponse first = dockerRule.getClient().inspectExecCmd(checkFileExec1.getId()).exec();
        assertThat(first.isRunning(), is(false));
        assertThat(first.getExitCode(), is(1));

        // Create the file
        ExecCreateCmdResponse touchFileExec = dockerRule.getClient().execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("touch", "/marker").exec();
        LOG.info("Created exec {}", touchFileExec.toString());
        assertThat(touchFileExec.getId(), not(is(emptyString())));
        dockerRule.getClient().execStartCmd(touchFileExec.getId()).withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
        InspectExecResponse second = dockerRule.getClient().inspectExecCmd(touchFileExec.getId()).exec();
        assertThat(second.isRunning(), is(false));
        assertThat(second.getExitCode(), is(0));

        // Check that file does exist now
        ExecCreateCmdResponse checkFileExec2 = dockerRule.getClient().execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("test", "-e", "/marker").exec();
        LOG.info("Created exec {}", checkFileExec2.toString());
        assertThat(checkFileExec2.getId(), not(is(emptyString())));
        dockerRule.getClient().execStartCmd(checkFileExec2.getId()).withDetach(false)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
        InspectExecResponse third = dockerRule.getClient().inspectExecCmd(checkFileExec2.getId()).exec();
        assertThat(third.isRunning(), is(false));
        assertThat(third.getExitCode(), is(0));

        // Get container info and check its roundtrip to ensure the consistency
        InspectContainerResponse containerInfo = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());
        JSONTestHelper.testRoundTrip(containerInfo);
    }

    @Test
    public void inspectExecNetworkSettings() throws IOException {
        final RemoteApiVersion apiVersion = getVersion(dockerRule.getClient());

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse exec = dockerRule.getClient().execCreateCmd(container.getId()).withAttachStdout(true)
                .withAttachStderr(true).withCmd("/bin/bash").exec();
        LOG.info("Created exec {}", exec.toString());
        assertThat(exec.getId(), not(is(emptyString())));

        InspectExecResponse inspectExecResponse = dockerRule.getClient().inspectExecCmd(exec.getId()).exec();

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
