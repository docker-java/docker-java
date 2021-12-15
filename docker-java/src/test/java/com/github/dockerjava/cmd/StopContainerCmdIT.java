package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

public class StopContainerCmdIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(StopContainerCmdIT.class);

    @Test
    public void testStopContainer() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        LOG.info("Stopping container: {}", container.getId());
        dockerRule.getClient().stopContainerCmd(container.getId()).withTimeout(2).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(false)));

        final Integer exitCode = inspectContainerResponse.getState().getExitCode();
        
        assertThat(exitCode, is(137));
        
    }

    @Test(expected = NotFoundException.class)
    public void testStopNonExistingContainer() throws DockerException {

        dockerRule.getClient().stopContainerCmd("non-existing").withTimeout(2).exec();
    }

}
