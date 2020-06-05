package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

public class ResizeContainerCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ResizeContainerCmdIT.class);

    @Test
    public void resizeExecTest() {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withUser("root")
                .withCmd("sh").withName(containerName).withTty(true).withStdinOpen(true).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        dockerRule.getClient().resizeContainerCmd(container.getId()).withSize(30, 120).exec();
    }
}
