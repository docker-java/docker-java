package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

import static com.github.dockerjava.utils.TestUtils.isNotSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class InspectContainerCmdIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(InspectContainerCmdIT.class);

    @Test
    public void inspectContainer() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse containerInfo = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());

    }

    @Test()
    public void inspectContainerWithSize() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerCmd command = dockerRule.getClient().inspectContainerCmd(container.getId()).withSize(true);
        assertTrue(command.getSize());
        InspectContainerResponse containerInfo = command.exec();
        assertEquals(containerInfo.getId(), container.getId());

        // TODO check swarm
        if (isNotSwarm(dockerRule.getClient())) {
            assertNotNull(containerInfo.getSizeRootFs());
            assertTrue(containerInfo.getSizeRootFs().intValue() > 0);
        }
    }

    @Test(expected = NotFoundException.class)
    public void inspectNonExistingContainer() throws DockerException {
        dockerRule.getClient().inspectContainerCmd("non-existing").exec();
    }

    @Test
    public void inspectContainerRestartCount() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getRestartCount(), equalTo(0));
    }

    @Test
    public void inspectContainerNetworkSettings() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertFalse(inspectContainerResponse.getNetworkSettings().getHairpinMode());
    }
}
