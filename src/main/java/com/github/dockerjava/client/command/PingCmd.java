package com.github.dockerjava.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 * Ping the Docker server
 * 
 */
public class PingCmd extends AbstrDockerCmd<PingCmd, Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingCmd.class);
    
    protected Integer impl() {
        WebResource webResource = baseResource.path("/_ping");

        try {
            LOGGER.trace("GET: {}", webResource);
            ClientResponse resp = webResource.get(ClientResponse.class);
            return resp.getStatus();
        } catch (UniformInterfaceException exception) {
            return exception.getResponse().getStatus();
        }
    }
}
