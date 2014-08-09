package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.github.dockerjava.client.model.Version;
import com.sun.jersey.api.client.WebResource;


/**
 * Returns the Docker version info.
 */
public class VersionCommand extends AbstrDockerCmd<VersionCommand, Version> implements VersionCmd  {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionCommand.class);
	
    @Override
    public String toString() {
        return "version";
    }   

	protected Version impl() throws DockerException {
		WebResource webResource = baseResource.path("/version");

		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(Version.class);
	}
}
