package com.github.dockerjava.cmd;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.RemoteApiVersion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

public class RestartContainerCmdImplIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(RestartContainerCmdImplIT.class);

    @Test
    public void restartContainer() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        String startTime = inspectContainerResponse.getState().getStartedAt();

        dockerRule.getClient().restartContainerCmd(container.getId()).withtTimeout(2).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect After Restart: {}", inspectContainerResponse2.toString());

        String startTime2 = inspectContainerResponse2.getState().getStartedAt();

        assertThat(startTime, not(equalTo(startTime2)));

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(true)));

        dockerRule.getClient().killContainerCmd(container.getId()).exec();
    }

    @Test
    public void restartContainerWithSignal() throws Exception {
        assumeThat("API version should be >= 1.42", dockerRule, isGreaterOrEqual(RemoteApiVersion.VERSION_1_42));

        DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
            .withApiVersion(RemoteApiVersion.VERSION_1_42)
            .withRegistryUrl("https://index.docker.io/v1/")
            .build();
        try (DockerClient dockerClient = createDockerClient(dockerClientConfig)) {
            String expectedUserSignal = "10";
            String initialCommandWithTrap = "trap 'echo \"exit trapped\"' %s; while :; do sleep 1; done";
            final String containerId = dockerClient
                .createContainerCmd(DEFAULT_IMAGE)
                .withCmd(
                    "/bin/sh",
                    "-c",
                    String.format(initialCommandWithTrap, expectedUserSignal))
                .exec()
                .getId();
            assertThat(containerId, not(is(emptyString())));
            dockerClient.startContainerCmd(containerId).exec();

            // Restart container without signal
            dockerClient.restartContainerCmd(containerId).exec();
            String log = dockerRule.containerLog(containerId);
            assertThat(log.trim(), emptyString());

            dockerClient.restartContainerCmd(containerId).withSignal(expectedUserSignal).exec();
            log = dockerRule.containerLog(containerId);
            assertThat(log.trim(), is("exit trapped"));

            dockerClient.removeContainerCmd(containerId).withForce(true).withRemoveVolumes(true).exec();
        }
    }

    @Test(expected = NotFoundException.class)
    public void restartNonExistingContainer() throws DockerException {

        dockerRule.getClient().restartContainerCmd("non-existing").exec();
    }
}
