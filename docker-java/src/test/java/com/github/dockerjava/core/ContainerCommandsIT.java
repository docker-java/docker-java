package com.github.dockerjava.core;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.AttachContainerSpec;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateContainerSpec;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.ListContainersSpec;
import com.github.dockerjava.api.command.StopContainerSpec;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.cmd.CmdIT;
import com.github.dockerjava.core.command.AttachContainerResultCallback;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContainerCommandsIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(ContainerCommandsIT.class);

    // Immutable, can be shared
    private static final CreateContainerSpec CONTAINER_SPEC = CreateContainerSpec.builder()
            .image("busybox")
            .cmd("top")
            .build();

    private DockerClient.ContainerCommands containerCommands;

    @Before
    public void setUp() {
        containerCommands = dockerRule.getClient().containerCommands();
    }

    @Test
    public void shouldCreateContainer() throws Exception {
        String containerName = "/generated_" + UUID.randomUUID();

        CreateContainerResponse container = containerCommands.create(
                CONTAINER_SPEC.withName(containerName).withCmd("ping", "-i", "0.2", "127.0.0.1")
        );

        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse containerInfo = containerCommands.inspect(container.getId());
        assertEquals(container.getId(), containerInfo.getId());
        assertEquals(containerName, containerInfo.getName());

        List<Container> containers = containerCommands.list(
                ListContainersSpec.builder()
                        .hasShowAllEnabled(true)
                        .putFilter("name", Arrays.asList(containerName))
                        .build()
        ).getContainers();

        assertEquals(1, containers.size());
        assertArrayEquals(new String[]{containerName}, containers.get(0).getNames());

        CountDownLatch latch = new CountDownLatch(5);

        containerCommands.attach(
                AttachContainerSpec.builder()
                        .containerId(container.getId())
                        .hasStdoutEnabled(true)
                        .hasStderrEnabled(true)
                        .hasFollowStreamEnabled(true)
                        .build(),
                new AttachContainerResultCallback() {
                    @Override
                    public void onNext(Frame item) {
                        super.onNext(item);
                        latch.countDown();
                    }
                }
        );

        containerCommands.start(container.getId());

        assertTrue(latch.await(10, TimeUnit.SECONDS));

        containerCommands.stop(
                StopContainerSpec.builder()
                        .containerId(container.getId())
                        .timeout(1)
                        .build()
        );
    }
}
