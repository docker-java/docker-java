package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.cmd.CmdIT;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.junit.category.Integration;
import com.github.dockerjava.junit.category.SwarmModeIntegration;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_24;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.awaitility.Awaitility.await;
import static org.junit.Assume.assumeThat;

@Category({SwarmModeIntegration.class, Integration.class})
public abstract class SwarmCmdIT extends CmdIT {

    private static final String DOCKER_IN_DOCKER_IMAGE_REPOSITORY = "docker";

    private static final String DOCKER_IN_DOCKER_IMAGE_TAG = "17.12-dind";

    private static final String DOCKER_IN_DOCKER_CONTAINER_PREFIX = "docker";

    private static final String NETWORK_NAME = "dind-network";

    private final AtomicInteger numberOfDockersInDocker = new AtomicInteger();

    private final Set<String> startedContainerIds = new HashSet<>();

    @Before
    public final void setUpMultiNodeSwarmCmdIT() throws Exception {
        assumeThat(dockerRule, isGreaterOrEqual(VERSION_1_24));
    }

    protected DockerClient startSwarm() {
        DockerClient dockerClient;
        try {
            dockerClient = startDockerInDocker();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        dockerClient.initializeSwarmCmd(new SwarmSpec()).exec();
        return dockerClient;
    }

    @After
    public final void tearDownMultiNodeSwarmCmdIT() {
        for (String containerId : startedContainerIds) {
            try {
                dockerRule.getClient().removeContainerCmd(containerId).withForce(true).exec();
            } catch (NotFoundException e) {
                // container does not exist
            }
        }

        try {
            dockerRule.getClient().removeNetworkCmd(NETWORK_NAME).exec();
        } catch (NotFoundException e) {
            // network does not exist
        }
    }

    protected DockerClient startDockerInDocker() throws InterruptedException {
        // Create network if not already exists
        DockerClient hostDockerClient = dockerRule.getClient();
        try {
            hostDockerClient.inspectNetworkCmd().withNetworkId(NETWORK_NAME).exec();
        } catch (NotFoundException e) {
            try {
                hostDockerClient.createNetworkCmd().withName(NETWORK_NAME).exec();
            } catch (ConflictException e2) {
                // already exists
            }
        }

        try (
            ResultCallback.Adapter<PullResponseItem> callback = hostDockerClient.pullImageCmd(DOCKER_IN_DOCKER_IMAGE_REPOSITORY)
                .withTag(DOCKER_IN_DOCKER_IMAGE_TAG)
                .start()
        ) {
            callback.awaitCompletion();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExposedPort exposedPort = ExposedPort.tcp(2375);
        CreateContainerResponse response = hostDockerClient
            .createContainerCmd(DOCKER_IN_DOCKER_IMAGE_REPOSITORY + ":" + DOCKER_IN_DOCKER_IMAGE_TAG)
            .withHostConfig(newHostConfig()
                .withNetworkMode(NETWORK_NAME)
                .withPortBindings(new PortBinding(
                    Ports.Binding.bindIp("127.0.0.1"),
                    exposedPort))
                .withPrivileged(true))
            .withAliases(DOCKER_IN_DOCKER_CONTAINER_PREFIX + numberOfDockersInDocker.incrementAndGet())
            .exec();

        String containerId = response.getId();
        startedContainerIds.add(containerId);

        hostDockerClient.startContainerCmd(containerId).exec();

        InspectContainerResponse inspectContainerResponse = hostDockerClient.inspectContainerCmd(containerId).exec();

        Ports.Binding binding = inspectContainerResponse.getNetworkSettings().getPorts().getBindings().get(exposedPort)[0];

        DockerClient dockerClient = initializeDockerClient(binding);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            dockerClient.pingCmd().exec();
        });

        return dockerClient;
    }

    private DockerClient initializeDockerClient(Ports.Binding binding) {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withRegistryUrl("https://index.docker.io/v1/")
            .withDockerHost("tcp://" + binding).build();
        return getFactoryType().createDockerClient(config);
    }
}
