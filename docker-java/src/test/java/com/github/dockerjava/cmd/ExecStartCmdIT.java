package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.security.SecureRandom;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static com.github.dockerjava.utils.TestUtils.asString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ExecStartCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ExecStartCmdIT.class);

    @Test
    public void execStart() throws Exception {
        assumeNotSwarm("no network in swarm", dockerRule);

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerRule.getClient().execCreateCmd(container.getId())
                .withAttachStdout(true)
                .withCmd("touch", "/execStartTest.log")
                .withUser("root")
                .exec();
        dockerRule.getClient().execStartCmd(execCreateCmdResponse.getId())
                .exec(new ExecStartResultCallback(System.out, System.err))
                .awaitCompletion();

        InputStream response = dockerRule.getClient().copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test
    public void execStartAttached() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerRule.getClient().execCreateCmd(container.getId())
                .withAttachStdout(true).withCmd("touch", "/execStartTest.log").exec();
        dockerRule.getClient().execStartCmd(execCreateCmdResponse.getId()).withDetach(false).withTty(true)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();

        InputStream response = dockerRule.getClient().copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test(expected = NotFoundException.class)
    public void execStartWithNonExistentUser() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerRule.getClient().execCreateCmd(container.getId())
                .withAttachStdout(true).withCmd("touch", "/execStartTest.log").withUser("NonExistentUser").exec();
        dockerRule.getClient().execStartCmd(execCreateCmdResponse.getId()).withDetach(false).withTty(true)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();

        dockerRule.getClient().copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();
    }
}
