package com.github.dockerjava.client.command;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmd extends AbstrDockerCmd<PullImageCmd, InputStream>  {

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


	protected InputStream impl() {

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("tag", tag);
		params.add("fromImage", repository);
		params.add("registry", registry);

		WebResource webResource = baseResource.path("/images/create").queryParams(params);

		LOGGER.trace("POST: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class).getEntityInputStream();
	}
}
