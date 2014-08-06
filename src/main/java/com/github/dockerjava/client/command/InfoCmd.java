package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.Info;
import com.sun.jersey.api.client.WebResource;


/**
 * Return Docker server info
 */
public class InfoCmd extends AbstrDockerCmd<InfoCmd, Info>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(InfoCmd.class);
	
    @Override
    public String toString() {
        return "info";
    }
    
	protected Info impl() throws DockerException {
		WebResource webResource = baseResource.path("/info");
		
		LOGGER.trace("GET: {}", webResource);
		return webResource.accept(MediaType.APPLICATION_JSON).get(Info.class);
	}
}
