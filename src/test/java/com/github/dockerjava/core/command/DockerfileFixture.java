package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerClient;

import java.io.File;

/**
 * Start and stop a single container for testing.
 */
public class DockerfileFixture implements AutoCloseable {

    private final DockerClient dockerClient;
    private String directory;
    private String repository;
    private String containerId;

    public DockerfileFixture(DockerClient dockerClient, String directory) {
        this.dockerClient = dockerClient;
        this.directory = directory;
    }

    public void open() throws Exception {

        dockerClient
                .buildImageCmd(new File("src/test/resources", directory))
                .withNoCache() // remove alternatives, cause problems
                .exec()
                .close();

        repository = dockerClient
                .listImagesCmd()
                .exec()
                .get(0)
                .getRepoTags()[0];

        containerId = dockerClient
                .createContainerCmd(repository)
                .exec()
                .getId();

        dockerClient
                .startContainerCmd(containerId)
                .exec();
    }

    @Override
    public void close() throws Exception {

        dockerClient
                .removeContainerCmd(containerId)
                .withForce() // stop too
                .exec();

        dockerClient
                .removeImageCmd(repository)
                .withForce()
                .exec();
    }

    public String getContainerId() {
        return containerId;
    }
}
