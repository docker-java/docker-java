package com.github.dockerjava.client.command;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.PingCmd;

/**
 * Ping the Docker server
 * 
 */
public class PingCommand extends AbstrDockerCmd<PingCommand, Void> implements PingCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(PingCommand.class);

	protected Void impl() {
		WebTarget webResource = baseResource.path("/_ping");
       
        LOGGER.trace("GET: {}", webResource);
        webResource.request().get(Response.class);
        
        return null;
	}
}
