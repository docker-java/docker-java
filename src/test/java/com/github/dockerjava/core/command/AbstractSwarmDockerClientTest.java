package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.AbstractJerseyDockerClientTest;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.lang.reflect.Method;

import static com.github.dockerjava.utils.TestUtils.getVersion;

public abstract class AbstractSwarmDockerClientTest extends AbstractJerseyDockerClientTest {
    protected DockerClient secondDockerClient;
    private int numberOfDockersInDocker = 0;

    private static final int PORT_START = 2378;
    private static final String DOCKER_IN_DOCKER_CONTAINER_PREFIX = "docker";
    private static final String NETWORK_NAME = "dind-network";
    private static final int MAX_CONNECT_ATTEMPTS = 5;
    private static final int CONNECT_ATTEMPT_INTERVAL = 200;
    private static final String DOCKER_IN_DOCKER_IMAGE_REPOSITORY = "docker";
    private static final String DOCKER_IN_DOCKER_IMAGE_TAG = "1.12-dind";

    public void beforeTest() throws Exception {
        super.beforeTest();

        final RemoteApiVersion apiVersion = getVersion(dockerClient);
        if (!apiVersion.isGreaterOrEqual(RemoteApiVersion.VERSION_1_24)) {
            throw new SkipException("API version should be >= 1.24");
        }
    }

    public void beforeMethod(Method method) {
        super.beforeMethod(method);
        leaveIfInSwarm();
    }

    @Override
    public void afterMethod(ITestResult result) {
        super.afterMethod(result);
        removeDockersInDocker();
    }

    protected void removeDockersInDocker() {
        for (int i = 1; i <= numberOfDockersInDocker; i++) {
            try {
                dockerClient.removeContainerCmd(DOCKER_IN_DOCKER_CONTAINER_PREFIX + i);
            } catch (NotFoundException e) {
                // container does not exist
            }
        }

        try {
            dockerClient.removeNetworkCmd(NETWORK_NAME).exec();
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
            dockerClient.removeContainerCmd(name).withForce(true).exec();
        } catch (NotFoundException e) {
            // container does not exist
        }

        // Create network if not already exists
        try {
            dockerClient.inspectNetworkCmd().withNetworkId(NETWORK_NAME).exec();
        } catch (NotFoundException e) {
            try {
                dockerClient.createNetworkCmd().withName(NETWORK_NAME).exec();
            } catch (ConflictException e2) {
                // already exists
            }
        }

        dockerClient.pullImageCmd(DOCKER_IN_DOCKER_IMAGE_REPOSITORY)
                .withTag(DOCKER_IN_DOCKER_IMAGE_TAG)
                .exec(new PullImageResultCallback())
                .awaitSuccess();

        int port = PORT_START + (numberOfDockersInDocker - 1);
        CreateContainerResponse response = dockerClient
                .createContainerCmd(DOCKER_IN_DOCKER_IMAGE_REPOSITORY + ":" + DOCKER_IN_DOCKER_IMAGE_TAG)
                .withPrivileged(true)
                .withName(name)
                .withNetworkMode(NETWORK_NAME)
                .withAliases(name)
                .withPortBindings(new PortBinding(
                        Ports.Binding.bindIpAndPort("127.0.0.1", port),
                        ExposedPort.tcp(2375)))
                .exec();
        dockerClient.startContainerCmd(response.getId()).exec();

        return initializeDockerClient(port);
    }

    private DockerClient initializeDockerClient(int port) {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withRegistryUrl("https://index.docker.io/v1/")
                .withDockerHost("tcp://localhost:" + port).build();
        return DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(initTestDockerCmdExecFactory())
                .build();
    }

    private void leaveIfInSwarm() {
        try {
            // force in case this is a swarm manager
            dockerClient.leaveSwarmCmd().withForceEnabled(true).exec();
        } catch (NotAcceptableException e) {
            // do nothing, node is not part of a swarm
        }
    }
}
