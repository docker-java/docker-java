package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.Version;
import javax.ws.rs.client.WebTarget;


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
		WebTarget webResource = baseResource.path("/version");

		try {
			LOGGER.trace("GET: {}", webResource);
			return webResource.request().accept(MediaType.APPLICATION_JSON).get(Version.class);
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error.", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
