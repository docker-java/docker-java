package com.github.dockerjava.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Ping the Docker server
 * 
 */
public class PingCmd extends AbstrDockerCmd<PingCmd, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingCmd.class);
    
    protected Integer impl() {
        WebTarget webResource = baseResource.path("/_ping");

        try {
            LOGGER.trace("GET: {}", webResource);
            Response resp = webResource.request().get(Response.class);
            return resp.getStatus();
        } catch (ClientErrorException exception) {
            return exception.getResponse().getStatus();
        }
    }
}
