package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.utils.ContainerUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

public class PauseCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(PauseCmdIT.class);
    
    @Test
    public void pauseRunningContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        ContainerUtils.startContainer(dockerRule.getClient(), container);

        ContainerUtils.pauseContainer(dockerRule.getClient(), container);
    }

    @Test(expected = NotFoundException.class)
    public void pauseNonExistingContainer() {

        dockerRule.getClient().pauseContainerCmd("non-existing").exec();
    }

    @Test(expected = DockerException.class)
    public void pauseStoppedContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        ContainerUtils.startContainer(dockerRule.getClient(), container);

        ContainerUtils.stopContainer(dockerRule.getClient(), container);

        dockerRule.getClient().pauseContainerCmd(container.getId()).exec();
    }

    @Test(expected = DockerException.class)
    public void pausePausedContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        ContainerUtils.startContainer(dockerRule.getClient(), container);

        ContainerUtils.pauseContainer(dockerRule.getClient(), container);

        dockerRule.getClient().pauseContainerCmd(container.getId()).exec();
    }

    @Test(expected = DockerException.class)
    public void pauseCreatedContainer() {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().pauseContainerCmd(container.getId()).exec();
    }
}
