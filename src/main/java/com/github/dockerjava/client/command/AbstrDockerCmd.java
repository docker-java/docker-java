package com.github.dockerjava.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import javax.ws.rs.client.WebTarget;

public abstract class AbstrDockerCmd<T extends AbstrDockerCmd<T, RES_T>, RES_T> implements DockerCmd<RES_T> {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstrDockerCmd.class);

	protected WebTarget baseResource;

	@SuppressWarnings("unchecked")
	public T withBaseResource(WebTarget baseResource) {
		this.baseResource = baseResource;
		return (T)this;
	}
	
	abstract RES_T impl();
	
	@Override
	public RES_T exec() {
		Preconditions.checkNotNull(baseResource, "baseResource was not specified");
		LOGGER.debug("Cmd: {}", this);
		return impl();
	}
}