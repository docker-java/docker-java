package com.github.dockerjava.client.command;

import java.io.InputStream;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.PushImageCmd;
import com.google.common.base.Preconditions;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;

/**
 * Push the latest image to the repository.
 *
 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public class PushImageCommand extends AbstrAuthCfgDockerCmd<PushImageCommand, InputStream> implements PushImageCmd  {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushImageCommand.class);

	private String name;

	public PushImageCommand(String name) {
		withName(name);
	}

    @Override
	public String getName() {
        return name;
    }

    /**
	 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
	@Override
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
   
    /**
     * @throws NotFoundException No such image
     */
	@Override
    public InputStream exec() throws NotFoundException {
    	return super.exec();
    }

	protected InputStream impl() {
		WebTarget webResource = baseResource.path("/images/" + name(name) + "/push");

		final String registryAuth = registryAuth();
		LOGGER.trace("POST: {}", webResource);
		return webResource
                .request()
				.header("X-Registry-Auth", registryAuth)
				.accept(MediaType.APPLICATION_JSON)
				.post(entity(Response.class, MediaType.APPLICATION_JSON)).readEntity(InputStream.class);
	}

	private String name(String name) {
		return name.contains("/") ? name : authConfig.getUsername();
	}
}
