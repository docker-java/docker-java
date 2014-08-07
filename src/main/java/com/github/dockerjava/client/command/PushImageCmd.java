package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;


/**
 * Push the latest image to the repository.
 *
 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public class PushImageCmd extends AbstrAuthCfgDockerCmd<PushImageCmd, Response>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushImageCmd.class);

	private String name;

	public PushImageCmd(String name) {
		withName(name);
	}

    public String getName() {
        return name;
    }

    /**
	 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	public PushImageCmd withName(String name) {
		Preconditions.checkNotNull(name, "name was not specified");
		this.name = name;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("push ")
            .append(name)
            .toString();
    }

	protected Response impl() {
		WebTarget webResource = baseResource.path("/images/" + name(name) + "/push");
		try {
			final String registryAuth = registryAuth();
			LOGGER.trace("POST: {}", webResource);
			return webResource
                    .request()
					.header("X-Registry-Auth", registryAuth)
					.accept(MediaType.APPLICATION_JSON)
					.post(entity(Response.class, MediaType.APPLICATION_JSON));
		} catch (ClientErrorException e) {
			throw new DockerException(e);
		}
	}

	private String name(String name) {
		return name.contains("/") ? name : authConfig.getUsername();
	}
}
