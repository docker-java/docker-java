package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.cmd.CmdIT;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.junit.category.Integration;
import com.github.dockerjava.junit.category.SwarmModeIntegration;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.categories.Category;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_24;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.junit.Assume.assumeThat;

@Category({SwarmModeIntegration.class, Integration.class})
public abstract class SwarmCmdIT extends CmdIT {
    protected DockerClient secondDockerClient;
    private int numberOfDockersInDocker = 0;

    private static final int PORT_START = 2378;
    private static final String DOCKER_IN_DOCKER_CONTAINER_PREFIX = "docker";
    private static final String NETWORK_NAME = "dind-network";
    private static final String DOCKER_IN_DOCKER_IMAGE_REPOSITORY = "docker";
    private static final String DOCKER_IN_DOCKER_IMAGE_TAG = "17.12-dind";

    @Before
    public void beforeTest() throws Exception {
        assumeThat(dockerRule, isGreaterOrEqual(VERSION_1_24));
    }

    @Before
    public void beforeMethod() {
        leaveIfInSwarm();
    }

    @After
    public void afterMethod() {
        removeDockersInDocker();
    }

    protected void removeDockersInDocker() {
        for (int i = 1; i <= numberOfDockersInDocker; i++) {
            try {
                dockerRule.getClient().removeContainerCmd(DOCKER_IN_DOCKER_CONTAINER_PREFIX + i).withForce(true).exec();
            } catch (NotFoundException e) {
                // container does not exist
            }
        }

        try {
            dockerRule.getClient().removeNetworkCmd(NETWORK_NAME).exec();
        } catch (NotFoundException e) {
            // network does not exist
        }

        numberOfDockersInDocker = 0;
    }

    protected DockerClient startDockerInDocker() {
        numberOfDockersInDocker++;
        String name = DOCKER_IN_DOCKER_CONTAINER_PREFIX + numberOfDockersInDocker;

        // Delete if already exists
        try {
            dockerRule.getClient().removeContainerCmd(name).withForce(true).exec();
        } catch (NotFoundException e) {
            // container does not exist
        }

        // Create network if not already exists
        try {
            dockerRule.getClient().inspectNetworkCmd().withNetworkId(NETWORK_NAME).exec();
        } catch (NotFoundException e) {
            try {
                dockerRule.getClient().createNetworkCmd().withName(NETWORK_NAME).exec();
            } catch (ConflictException e2) {
                // already exists
            }
        }

        dockerRule.getClient().pullImageCmd(DOCKER_IN_DOCKER_IMAGE_REPOSITORY)
                .withTag(DOCKER_IN_DOCKER_IMAGE_TAG)
                .exec(new PullImageResultCallback())
                .awaitSuccess();

        int port = PORT_START + (numberOfDockersInDocker - 1);
        CreateContainerResponse response = dockerRule.getClient()
                .createContainerCmd(DOCKER_IN_DOCKER_IMAGE_REPOSITORY + ":" + DOCKER_IN_DOCKER_IMAGE_TAG)
                .withHostConfig(newHostConfig()
                        .withNetworkMode(NETWORK_NAME)
                        .withPortBindings(new PortBinding(
                                Ports.Binding.bindIpAndPort("127.0.0.1", port),
                                ExposedPort.tcp(2375)))
                        .withPrivileged(true))
                .withName(name)
                .withAliases(name)

                .exec();

        dockerRule.getClient().startContainerCmd(response.getId()).exec();

        return initializeDockerClient(port);
    }

    private DockerClient initializeDockerClient(int port) {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryUrl("https://index.docker.io/v1/")
                .withDockerHost("tcp://localhost:" + port).build();
        return DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(getFactoryType().createExecFactory())
                .build();
    }

    private void leaveIfInSwarm() {
        try {
            // force in case this is a swarm manager
            dockerRule.getClient().leaveSwarmCmd().withForceEnabled(true).exec();
        } catch (NotAcceptableException e) {
            // do nothing, node is not part of a swarm
        } catch (DockerException ex) {
            if (!ex.getMessage().contains("node is not part of a swarm")) {
                throw ex;
            }
        }
    }
}
