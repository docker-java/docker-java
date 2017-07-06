package com.github.dockerjava.cmd;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.security.SecureRandom;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;

public class ResizeCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ResizeCmdIT.class);

    @Test
    public void execResize() throws Exception {
        assumeNotSwarm("no network in swarm", dockerRule);

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sh")
                .withName(containerName).withTty(true).withStdinOpen(true).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerRule.getClient().execCreateCmd(container.getId())
                .withAttachStdout(true).withTty(true).withCmd("/bin/sh").exec();
        dockerRule.getClient().execStartCmd(execCreateCmdResponse.getId()).exec(
                new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
        dockerRule.getClient().resizeExecCmd(execCreateCmdResponse.getId()).withSize(1024, 1024);
    }

    @Test
    public void containerResize() throws Exception {
        assumeNotSwarm("no network in swarm", dockerRule);

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sh")
                .withName(containerName).withTty(true).withStdinOpen(true).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        dockerRule.getClient().resizeContainerCmd(container.getId()).withSize(1024, 1024).exec();
    }
}
