package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

public class RemoveContainerCmdImplIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(RemoveContainerCmdImplIT.class);
    

    @Test
    public void removeContainer() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true").exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        dockerRule.getClient().waitContainerCmd(container.getId()).start().awaitStatusCode();

        LOG.info("Removing container: {}", container.getId());
        dockerRule.getClient().removeContainerCmd(container.getId()).exec();

        List<Container> containers2 = dockerRule.getClient().listContainersCmd().withShowAll(true).exec();

        Matcher matcher = not(hasItem(hasField("id", startsWith(container.getId()))));
        assertThat(containers2, matcher);

    }

    @Test(expected = NotFoundException.class)
    public void removeNonExistingContainer() throws DockerException {

        dockerRule.getClient().removeContainerCmd("non-existing").exec();
    }

}
