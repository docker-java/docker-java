package com.github.dockerjava.client.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.BadRequestException;
import com.github.dockerjava.api.ConflictException;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotAcceptableException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public abstract class AbstrDockerCmd<T extends AbstrDockerCmd<T, RES_T>, RES_T> implements DockerCmd<RES_T> {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstrDockerCmd.class);

	protected WebResource baseResource;

	@SuppressWarnings("unchecked")
	public T withBaseResource(WebResource baseResource) {
		this.baseResource = baseResource;
		return (T) this;
	}
	
	protected abstract RES_T impl();
	
	/**
	 * @throws DockerException If something gets wrong
	 */
	@Override
	public RES_T exec() throws DockerException {
		Preconditions.checkNotNull(baseResource, "baseResource was not specified");
		LOGGER.debug("Cmd: {}", this);
		
		try {
			return impl();
		} catch (UniformInterfaceException exception) {
			int status = exception.getResponse().getStatus();
			switch(status) {
				case 204: return null;
				case 304: throw new NotModifiedException(exception);
				case 400: throw new BadRequestException(exception);
				case 404: throw new NotFoundException(exception);
				case 406: throw new NotAcceptableException(exception);
				case 409: throw new ConflictException(exception);
				case 500: throw new InternalServerErrorException(exception);
				default: throw toDockerException(exception);
			}
		}		
	}
	
	protected DockerException toDockerException(UniformInterfaceException exception) {
		return new DockerException(exception.getMessage(), exception.getResponse().getStatus(), exception);
	} 
}