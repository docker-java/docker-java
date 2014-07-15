package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.github.dockerjava.client.model.ContainerCreateResponse;
import com.github.dockerjava.client.model.CreateContainerConfig;
import com.github.dockerjava.client.model.ExposedPort;
import com.github.dockerjava.client.model.Volume;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;


/**
 * 
 * Creates a new container.
 *
 */
public class CreateContainerCmd extends AbstrDockerCmd<CreateContainerCmd, ContainerCreateResponse>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateContainerCmd.class);
	
	private CreateContainerConfig containerCreateConfig;
	private String name;
	
	public CreateContainerCmd(String image) {
		this(new CreateContainerConfig());
		Preconditions.checkNotNull(image, "image was not specified");
		this.containerCreateConfig.withImage(image);
	}
	
	public CreateContainerCmd(CreateContainerConfig config) {
		Preconditions.checkNotNull(config, "config was not specified");
		this.containerCreateConfig = config;
	}
	
	public CreateContainerCmd withImage(String image) {
		Preconditions.checkNotNull(image, "image was not specified");
		this.containerCreateConfig.withImage(image);
		return this;
	}
	
	public CreateContainerCmd withCmd(String... cmd) {
		Preconditions.checkNotNull(cmd, "cmd was not specified");
		this.containerCreateConfig.withCmd(cmd);
		return this;
	}
	
	public CreateContainerCmd withVolumes(Volume... volumes) {
		Preconditions.checkNotNull(volumes, "volumes was not specified");
		this.containerCreateConfig.withVolumes(volumes);
		return this;
	}
    
        public CreateContainerCmd withEnv(String... env) {
                Preconditions.checkNotNull(env, "env was not specified");
                this.containerCreateConfig.withEnv(env);
                return this;
        }

        public CreateContainerCmd withHostName(String hostName) {
                Preconditions.checkNotNull(hostName, "hostName was not specified");
                this.containerCreateConfig.withHostName(hostName);
                return this;
        }	
	
	public CreateContainerCmd withName(String name) {
		Preconditions.checkNotNull(name, "name was not specified");
		this.name = name;
		return this;
	}
	
	public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts) {
		Preconditions.checkNotNull(exposedPorts, "exposedPorts was not specified");
		
		this.containerCreateConfig.withExposedPorts(exposedPorts);
		return this;
	}
	
    @Override
    public String toString() {
        return new StringBuilder("create container ")
            .append(name != null ? "name=" + name + " " : "")
            .append(containerCreateConfig)
            .toString();
    }

	protected ContainerCreateResponse impl() {
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		if (name != null) {
			params.add("name", name);
		}
		WebResource webResource = baseResource.path("/containers/create").queryParams(params);

		try {
			LOGGER.trace("POST: {} ", webResource);
			return webResource.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.post(ContainerCreateResponse.class, containerCreateConfig);
		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("%s is an unrecognized image. Please pull the image first.", containerCreateConfig.getImage()));
			} else if (exception.getResponse().getStatus() == 406) {
				throw new DockerException("impossible to attach (container not running)");
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}
}
