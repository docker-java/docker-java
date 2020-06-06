package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class ResizeExecCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ResizeExecCmdIT.class);

    private static final int TTY_HEIGHT = 30;
    private static final int TTY_WIDTH = 120;

    @Test
    public void resizeExecInstanceTtyTest() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withUser("root")
                .withCmd("sleep", "9999").withName(containerName).exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        // wait until tty size changed to target size
        ExecCreateCmdResponse execCreateCmdResponse = dockerRule.getClient().execCreateCmd(container.getId()).withTty(true)
            .withAttachStdout(true).withAttachStderr(true)
            .withCmd("sh", "-c", String.format("until stty size | grep '%d %d'; do : ; done", TTY_HEIGHT, TTY_WIDTH)).exec();

        final ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback(System.out, System.err);

        dockerRule.getClient().execStartCmd(execCreateCmdResponse.getId()).exec(execStartResultCallback).awaitStarted();

        dockerRule.getClient().resizeExecCmd(execCreateCmdResponse.getId()).withSize(TTY_HEIGHT, TTY_WIDTH).exec();

        // time out, exec instance resize failed
        boolean waitResult = execStartResultCallback.awaitCompletion(10, TimeUnit.SECONDS);

        assertThat(waitResult, equalTo(true));
    }
}
