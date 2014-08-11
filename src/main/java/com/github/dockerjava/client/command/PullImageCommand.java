package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PullImageCmd;
import com.google.common.base.Preconditions;

/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCommand extends AbstrDockerCmd<PullImageCommand, InputStream> implements PullImageCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(PullImageCommand.class);

	private String repository, tag, registry;

	public PullImageCommand(String repository) {
		withRepository(repository);
	}

    @Override
	public String getRepository() {
        return repository;
    }

    @Override
	public String getTag() {
        return tag;
    }

    @Override
	public String getRegistry() {
        return registry;
    }

    @Override
	public PullImageCmd withRepository(String repository) {
		Preconditions.checkNotNull(repository, "repository was not specified");
		this.repository = repository;
		return this;
	}

	@Override
	public PullImageCmd withTag(String tag) {
		Preconditions.checkNotNull(tag, "tag was not specified");
		this.tag = tag;
		return this;
	}

	@Override
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

	protected InputStream impl() {

		WebTarget webResource = baseResource.path("/images/create")
                .queryParam("tag", tag)
                .queryParam("fromImage", repository)
                .queryParam("registry", registry);
		
		LOGGER.trace("POST: {}", webResource);
		return webResource.request()
				.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.post(entity(Response.class, MediaType.APPLICATION_JSON)).readEntity(InputStream.class);
	}
}
