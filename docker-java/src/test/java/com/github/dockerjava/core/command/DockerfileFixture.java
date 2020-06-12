package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Start and stop a single container for testing.
 */
public class DockerfileFixture implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerfileFixture.class);

    private final DockerClient client;

    private String directory;

    private String repository;

    private String containerId;

    public DockerfileFixture(DockerClient client, String directory) {
        this.client = client;
        this.directory = directory;
    }

    public void open() throws Exception {

        LOGGER.info("building {}", directory);

        client.buildImageCmd(new File("src/test/resources", directory)).withNoCache(true)
                .start().awaitImageId();

        Image lastCreatedImage = client.listImagesCmd().exec().get(0);

        repository = lastCreatedImage.getRepoTags()[0];

        LOGGER.info("created {} {}", lastCreatedImage.getId(), repository);

        containerId = client.createContainerCmd(lastCreatedImage.getId()).exec().getId();

        LOGGER.info("starting {}", containerId);

        client.startContainerCmd(containerId).exec();
    }

    @Override
    public void close() throws Exception {

        if (containerId != null) {
            LOGGER.info("removing container {}", containerId);
            try {
                client.removeContainerCmd(containerId).withForce(true) // stop too
                        .exec();
            } catch (NotFoundException | InternalServerErrorException ignored) {
                LOGGER.info("ignoring {}", ignored.getMessage());
            }
            containerId = null;
        }

        if (repository != null) {
            LOGGER.info("removing repository {}", repository);
            try {
                client.removeImageCmd(repository).withForce(true).exec();
            } catch (DockerException e) {
                LOGGER.info("ignoring {}", e.getMessage());
            }
            repository = null;
        }
    }

    public String getContainerId() {
        return containerId;
    }
}
