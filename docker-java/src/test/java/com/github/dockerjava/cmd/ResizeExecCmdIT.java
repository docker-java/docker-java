package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

public class ResizeExecCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ResizeExecCmdIT.class);

    @Test
    public void resizeExecTest() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withUser("root")
                .withCmd("sleep", "9999").withName(containerName).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerRule.getClient().execCreateCmd(container.getId()).withTty(true)
                 .withAttachStdout(true).withAttachStderr(true).withCmd("top").exec();

        final ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback(System.out, System.err);

        Thread execThread = new Thread(() -> {
            try {
                execStartResultCallback.awaitCompletion();
            } catch (InterruptedException ignored) {
            }
        });

        dockerRule.getClient().execStartCmd(execCreateCmdResponse.getId()).exec(execStartResultCallback);
        execThread.start();
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));

        dockerRule.getClient().resizeExecCmd(execCreateCmdResponse.getId()).withSize(30, 120).exec();
    }
}
