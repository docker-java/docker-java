package com.github.dockerjava.client.command;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;


/**
 * Pull image from repository.
 */
public class PullImageCmd extends AbstrDockerCmd<PullImageCmd, Response> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCmd.class);

    private String repository, tag, registry;

    public PullImageCmd(String repository) {
        withRepository(repository);
    }

    public String getRepository() {
        return repository;
    }

    public String getTag() {
        return tag;
    }

    public String getRegistry() {
        return registry;
    }

    public PullImageCmd withRepository(String repository) {
        Preconditions.checkNotNull(repository, "repository was not specified");
        this.repository = repository;
        return this;
    }

    public PullImageCmd withTag(String tag) {
        Preconditions.checkNotNull(tag, "tag was not specified");
        this.tag = tag;
        return this;
    }

    public PullImageCmd withRegistry(String registry) {
        Preconditions.checkNotNull(registry, "registry was not specified");
        this.registry = registry;
        return this;
    }

    @Override
    public String toString() {
        return new StringBuilder("pull ")
                .append(repository)
                .append(tag != null ? ":" + tag : "")
                .toString();
    }

    protected Response impl() {
        Preconditions.checkNotNull(repository, "Repository was not specified");

        WebTarget webResource = baseResource
                .path("/images/create")
                .queryParam("tag", tag)
                .queryParam("fromImage", repository)
                .queryParam("registry", registry);

        try {
            LOGGER.trace("POST: {}", webResource);
            return webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(entity(Response.class, MediaType.APPLICATION_JSON));
        } catch (ClientErrorException exception) {
            if (exception.getResponse().getStatus() == 500) {
                throw new DockerException("Server error.", exception);
            } else {
                throw new DockerException(exception);
            }
        }
    }
}
