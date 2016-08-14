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
     * Starts container and ensures that it running
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
     * Pauses container and ensures that it paused
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
     * Stops container and ensures that it stopped
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
     * Unpauses container and ensures that it unpaused (running)
     *
     * @param dockerClient docker client
     * @param container    container
     */
    public static void unpauseContainer(DockerClient dockerClient, CreateContainerResponse container) {
        dockerClient.unpauseContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getState().getPaused(), is(false));
        assertThat(inspectContainerResponse.getState().getRunning(), is(true));
    }
}
