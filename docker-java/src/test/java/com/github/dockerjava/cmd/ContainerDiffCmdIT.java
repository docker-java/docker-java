package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ChangeLog;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ch.lambdaj.Lambda.selectUnique;
import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

public class ContainerDiffCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(ContainerDiffCmdIT.class);

    @Test
    public void testContainerDiff() throws DockerException {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("touch", "/test").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId()).start()
                .awaitStatusCode();
        assertThat(exitCode, equalTo(0));

        List<ChangeLog> filesystemDiff = dockerRule.getClient().containerDiffCmd(container.getId()).exec();
        LOG.info("Container DIFF: {}", filesystemDiff.toString());

        assertThat(filesystemDiff.size(), equalTo(1));
        ChangeLog testChangeLog = selectUnique(filesystemDiff, hasField("path", equalTo("/test")));

        assertThat(testChangeLog, hasField("path", equalTo("/test")));
        assertThat(testChangeLog, hasField("kind", equalTo(1)));
    }

    @Test(expected = NotFoundException.class)
    public void testContainerDiffWithNonExistingContainer() throws DockerException {

        dockerRule.getClient().containerDiffCmd("non-existing").exec();
    }
}
