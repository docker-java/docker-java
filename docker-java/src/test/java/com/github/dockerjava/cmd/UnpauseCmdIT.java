package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.utils.ContainerUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

public class UnpauseCmdIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(UnpauseCmdIT.class);

    @Test
    public void unpausePausedContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        ContainerUtils.startContainer(dockerRule.getClient(), container);

        ContainerUtils.pauseContainer(dockerRule.getClient(), container);

        ContainerUtils.unpauseContainer(dockerRule.getClient(), container);
    }

    @Test(expected = InternalServerErrorException.class)
    public void unpauseRunningContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        ContainerUtils.startContainer(dockerRule.getClient(), container);

        dockerRule.getClient().unpauseContainerCmd(container.getId()).exec();
    }

    @Test(expected = InternalServerErrorException.class)
    public void unpauseStoppedContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        ContainerUtils.startContainer(dockerRule.getClient(), container);

        ContainerUtils.stopContainer(dockerRule.getClient(), container);

        dockerRule.getClient().unpauseContainerCmd(container.getId()).exec();
    }

    @Test(expected = NotFoundException.class)
    public void unpauseNonExistingContainer() {

        dockerRule.getClient().unpauseContainerCmd("non-existing").exec();
    }

    @Test(expected = InternalServerErrorException.class)
    public void unpauseCreatedContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().unpauseContainerCmd(container.getId()).exec();
    }

    @Test(expected = InternalServerErrorException.class)
    public void unpauseUnpausedContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        ContainerUtils.startContainer(dockerRule.getClient(), container);

        ContainerUtils.pauseContainer(dockerRule.getClient(), container);

        dockerRule.getClient().unpauseContainerCmd(container.getId()).exec();
        dockerRule.getClient().unpauseContainerCmd(container.getId()).exec();
    }
}
