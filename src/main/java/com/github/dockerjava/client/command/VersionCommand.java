package com.github.dockerjava.client.command;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.Version;


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
		WebTarget webResource = baseResource.path("/version");
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON).get(Version.class);
	}
}
