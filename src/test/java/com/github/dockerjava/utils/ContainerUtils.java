package com.github.dockerjava.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Container cmd utils
 */
public class ContainerUtils {

    private ContainerUtils() {
    }

    /**
     * Start specified container
     *
     * @param dockerClient docker client
     * @param container    container
     */
    public static void startContainer(DockerClient dockerClient, CreateContainerResponse container) {
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getState().getRunning(), is(true));
    }

    /**
     * Pause specified container
     *
     * @param dockerClient docker client
     * @param container    container
     */
    public static void pauseContainer(DockerClient dockerClient, CreateContainerResponse container) {
        dockerClient.pauseContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getState().getPaused(), is(true));
    }

    /**
     * Stop specified container
     *
     * @param dockerClient docker client
     * @param container    container
     */
    public static void stopContainer(DockerClient dockerClient, CreateContainerResponse container) {
        dockerClient.stopContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getState().getRunning(), is(false));
    }

    /**
     * Unpause specified container
     *
     * @param dockerClient docker client
     * @param container    container
     */
    public static void unpaseContainer(DockerClient dockerClient, CreateContainerResponse container) {
        dockerClient.unpauseContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getState().getPaused(), is(false));
        assertThat(inspectContainerResponse.getState().getRunning(), is(true));
    }
}
