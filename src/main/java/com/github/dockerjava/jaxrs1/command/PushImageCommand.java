package com.github.dockerjava.jaxrs1.command;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.client.command.AbstrAuthCfgDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


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
		WebResource webResource = baseResource.path("/images/" + name(name) + "/push");
		
		final String registryAuth = registryAuth();
		LOGGER.trace("POST: {}", webResource);
		return webResource
				.header("X-Registry-Auth", registryAuth)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class).getEntityInputStream();
	}

	private String name(String name) {
		return name.contains("/") ? name : authConfig.getUsername();
	}
}
