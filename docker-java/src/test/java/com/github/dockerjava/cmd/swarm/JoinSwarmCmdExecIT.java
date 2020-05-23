package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.LocalNodeState;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.api.model.Swarm;
import com.github.dockerjava.api.model.SwarmJoinTokens;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class JoinSwarmCmdExecIT extends SwarmCmdIT {

    private static final int PORT_START = 2378;

    private static final String DOCKER_IN_DOCKER_IMAGE_REPOSITORY = "docker";

    private static final String DOCKER_IN_DOCKER_IMAGE_TAG = "17.12-dind";

    private static final String DOCKER_IN_DOCKER_CONTAINER_PREFIX = "docker";

    private static final String NETWORK_NAME = "dind-network";

    public static final Logger LOG = LoggerFactory.getLogger(JoinSwarmCmdExecIT.class);

    private int numberOfDockersInDocker = 0;

    private DockerClient docker1;

    private DockerClient docker2;

    @Before
    public void setUp() throws Exception {
        docker1 = startDockerInDocker();
        docker2 = startDockerInDocker();
    }

    @After
    public void afterMethod() {
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

    private SwarmJoinTokens initSwarmOnDocker(DockerClient docker) {
        SwarmSpec swarmSpec = new SwarmSpec();
        docker.initializeSwarmCmd(swarmSpec)
                .exec();
        LOG.info("Initialized swarm docker1: {}", swarmSpec.toString());

        Swarm swarm = docker.inspectSwarmCmd().exec();
        LOG.info("Inspected swarm on docker1: {}", swarm.toString());
        return swarm.getJoinTokens();
    }

    @Test
    public void joinSwarmAsWorker() throws Exception {
        SwarmJoinTokens tokens = initSwarmOnDocker(docker1);

        docker2.joinSwarmCmd()
                .withRemoteAddrs(Lists.newArrayList("docker1"))
                .withJoinToken(tokens.getWorker())
                .exec();
        LOG.info("docker2 joined docker1's swarm");

        Info info = docker2.infoCmd().exec();
        LOG.info("Inspected docker2: {}", info.toString());
        assertThat(info.getSwarm().getLocalNodeState(), is(equalTo(LocalNodeState.ACTIVE)));
    }

    @Test
    public void joinSwarmAsManager() throws DockerException, InterruptedException {
        SwarmJoinTokens tokens = initSwarmOnDocker(docker1);

        docker2.joinSwarmCmd()
                .withRemoteAddrs(Lists.newArrayList("docker1"))
                .withJoinToken(tokens.getManager())
                .exec();
        LOG.info("docker2 joined docker1's swarm");

        Info info = docker2.infoCmd().exec();
        LOG.info("Inspected docker2: {}", info.toString());
        assertThat(info.getSwarm().getLocalNodeState(), is(equalTo(LocalNodeState.ACTIVE)));
    }

    @Test(expected = DockerException.class)
    public void joinSwarmIfAlreadyInSwarm() throws Exception {
        SwarmJoinTokens tokens = initSwarmOnDocker(docker1);

        initSwarmOnDocker(docker2);

        docker2.joinSwarmCmd()
                .withRemoteAddrs(Lists.newArrayList("docker1"))
                .withJoinToken(tokens.getWorker())
                .exec();
    }


    private DockerClient startDockerInDocker() throws InterruptedException {
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
            .start()
            .awaitCompletion();

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
}
