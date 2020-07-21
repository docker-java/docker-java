package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.github.dockerjava.utils.TestUtils.isNotSwarm;
import static com.github.dockerjava.utils.TestUtils.isSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
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
        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse containerInfo = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());

    }

    @Test
    public void inspectContainerNodeProperty() throws DockerException {
        Map<String, String> label = Collections.singletonMap("inspectContainerNodeProperty", UUID.randomUUID().toString());
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withLabels(label)
                .exec();

        Container containerResult = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(label)
                .exec()
                .get(0);

        String name = containerResult.getNames()[0];

        InspectContainerResponse containerInfo = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        InspectContainerResponse.Node node = containerInfo.getNode();
        if (isSwarm(dockerRule.getClient())) {
            assertThat(node, is(notNullValue()));
            assertThat(node.getAddr(), is(notNullValue()));
            assertThat(node.getId(), is(notNullValue()));
            assertThat(node.getIp(), is(notNullValue()));
            assertThat(node.getLabels(), is(notNullValue()));
            assertThat(node.getLabels().get("com.github.dockerjava.test"), is("docker-java"));
            assertThat(node.getCpus(), is(greaterThanOrEqualTo(1)));
            assertThat(node.getMemory(), is(greaterThanOrEqualTo(64 * 1024 * 1024L)));
            assertThat("/" + node.getName() + containerInfo.getName(), is(name));
        } else {
            assertThat(node, is(nullValue()));
        }
    }

    @Test()
    public void inspectContainerWithSize() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

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

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getRestartCount(), equalTo(0));
    }

    @Test
    public void inspectContainerNetworkSettings() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertFalse(inspectContainerResponse.getNetworkSettings().getHairpinMode());
    }

    @Test
    public void inspectContainerNanoCPUs() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
            .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getHostConfig().getNanoCPUs(), is(0L));
    }
}
