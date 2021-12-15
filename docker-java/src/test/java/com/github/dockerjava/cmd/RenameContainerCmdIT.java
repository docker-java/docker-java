package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotEquals;

public class RenameContainerCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(RenameContainerCmdIT.class);

    @Test
    public void renameContainer() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        String name1 = inspectContainerResponse.getName();

        dockerRule.getClient().renameContainerCmd(container.getId())
                .withName(dockerRule.getKind() + "renameContainer")
                .exec();

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect After Rename: {}", inspectContainerResponse2.toString());

        String name2 = inspectContainerResponse2.getName();

        assertNotEquals(name1, name2);

        dockerRule.getClient().killContainerCmd(container.getId()).exec();
    }

    @Test(expected = NotFoundException.class)
    public void renameExistingContainer() throws DockerException, InterruptedException {
        dockerRule.getClient().renameContainerCmd("non-existing")
                .withName(dockerRule.getKind() + "renameExistingContainer")
                .exec();
    }
}
