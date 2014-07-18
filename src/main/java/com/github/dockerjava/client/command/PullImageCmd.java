package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmd extends AbstrDockerCmd<PullImageCmd, ClientResponse>  {

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

	protected ClientResponse impl() {
		Preconditions.checkNotNull(repository, "Repository was not specified");

		if (StringUtils.countMatches(repository, ":") == 1) {
			String repositoryTag[] = StringUtils.split(repository, ':');
			repository = repositoryTag[0];
			tag = repositoryTag[1];

		}

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("tag", tag);
		params.add("fromImage", repository);
		params.add("registry", registry);

		WebResource webResource = baseResource.path("/images/create").queryParams(params);

		try {
			LOGGER.trace("POST: {}", webResource);
			return webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
