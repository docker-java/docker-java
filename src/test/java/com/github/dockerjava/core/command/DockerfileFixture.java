package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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

        LOGGER.info("created {}", repository);

        containerId = dockerClient
                .createContainerCmd(repository)
                .exec()
                .getId();

        LOGGER.info("starting {}", containerId);

        dockerClient
                .startContainerCmd(containerId)
                .exec();
    }

    @Override
    public void close() throws Exception {

        if (containerId != null) {
            LOGGER.info("removing container {}", containerId);
            dockerClient
                    .removeContainerCmd(containerId)
                    .withForce() // stop too
                    .exec();
            containerId = null;
        }

        if (repository != null) {
            LOGGER.info("removing repostiory {}", repository);
            dockerClient
                    .removeImageCmd(repository)
                    .withForce()
                    .exec();
            repository = null;
        }
    }

    public String getContainerId() {
        return containerId;
    }
}
