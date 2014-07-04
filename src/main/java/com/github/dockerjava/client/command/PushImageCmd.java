package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;


/**
 * Push the latest image to the repository.
 *
 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
 */
public class PushImageCmd extends AbstrAuthCfgDockerCmd<PushImageCmd, ClientResponse>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushImageCmd.class);
	
	private String name;
	
	public PushImageCmd(String name) {
		withName(name);
	}
	
	/**
	 * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
	 */
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

	protected ClientResponse impl() {
		WebResource webResource = baseResource.path("/images/" + name(name) + "/push");
		try {
			final String registryAuth = registryAuth();
			LOGGER.trace("POST: {}", webResource);
			return webResource
					.header("X-Registry-Auth", registryAuth)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class);
		} catch (UniformInterfaceException e) {
			throw new DockerException(e);
		}
	}
	
	private String name(String name) {
		return name.contains("/") ? name : authConfig.getUsername();
	}
}
