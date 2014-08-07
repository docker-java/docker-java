package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.client.model.Version;
import com.sun.jersey.api.client.WebResource;


/**
 * Return the Docker version info.
 */
public class VersionCmd extends AbstrDockerCmd<VersionCmd, Version>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(VersionCmd.class);
	
	
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
