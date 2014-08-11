package com.github.dockerjava.client.command;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;

/**
 * Return Docker server info
 */
public class InfoCommand extends AbstrDockerCmd<InfoCommand, Info> implements InfoCmd  {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfoCommand.class);
	
	@Override
    public String toString() {
        return "info";
    }
    
	protected Info impl() throws DockerException {
		WebTarget webResource = baseResource.path("/info");

		LOGGER.trace("GET: {}", webResource);
		return webResource.request().accept(MediaType.APPLICATION_JSON).get(Info.class);
	}
}
