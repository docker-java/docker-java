package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ResizeContainerCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ResizeContainerCmdIT.class);

    private static final int TTY_HEIGHT = 30;
    private static final int TTY_WIDTH = 120;

    @Test
    public void resizeContainerTtyTest() {
        String containerName = "generated_" + new SecureRandom().nextInt();

        // wait until tty size changed to target size
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withUser("root")
            .withCmd("sh", "-c", String.format("until stty size | grep '%d %d'; do : ; done", TTY_HEIGHT, TTY_WIDTH))
            .withName(containerName).withTty(true).withStdinOpen(true).exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        dockerRule.getClient().resizeContainerCmd(container.getId()).withSize(TTY_HEIGHT, TTY_WIDTH).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId()).start()
            .awaitStatusCode(10, TimeUnit.SECONDS);

        LOG.info("Container exit code: {}", exitCode);

        assertThat(exitCode, equalTo(0));
    }
}
