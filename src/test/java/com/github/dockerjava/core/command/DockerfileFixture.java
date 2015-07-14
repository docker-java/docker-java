package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.AbstractDockerClientTest.BuildLogCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Start and stop a single container for testing.
 */
public class DockerfileFixture implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerfileFixture.class);

    private final DockerClient dockerClient;

    private String directory;

    private String repository;

    private String containerId;

    public DockerfileFixture(DockerClient dockerClient, String directory) {
        this.dockerClient = dockerClient;
        this.directory = directory;
    }

    public void open() throws Exception {

        LOGGER.info("building {}", directory);
        BuildLogCallback resultCallback = new BuildLogCallback();

        dockerClient.buildImageCmd(new File("src/test/resources", directory), resultCallback).withNoCache() // remove
                                                                                                                   // alternatives,
                                                                                                                   // cause
                                                                                                                   // problems
                .exec();

        resultCallback.awaitImageId();

        Image lastCreatedImage = dockerClient.listImagesCmd().exec().get(0);

        repository = lastCreatedImage.getRepoTags()[0];

        LOGGER.info("created {} {}", lastCreatedImage.getId(), repository);

        containerId = dockerClient.createContainerCmd(lastCreatedImage.getId()).exec().getId();

        LOGGER.info("starting {}", containerId);

        dockerClient.startContainerCmd(containerId).exec();
    }

    @Override
    public void close() throws Exception {

        if (containerId != null) {
            LOGGER.info("removing container {}", containerId);
            try {
                dockerClient.removeContainerCmd(containerId).withForce() // stop too
                        .exec();
            } catch (NotFoundException | InternalServerErrorException ignored) {
                LOGGER.info("ignoring {}", ignored.getMessage());
            }
            containerId = null;
        }

        if (repository != null) {
            LOGGER.info("removing repository {}", repository);
            try {
                dockerClient.removeImageCmd(repository).withForce().exec();
            } catch (NotFoundException | InternalServerErrorException e) {
                LOGGER.info("ignoring {}", e.getMessage());
            }
            repository = null;
        }
    }

    public String getContainerId() {
        return containerId;
    }
}
