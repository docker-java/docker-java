package com.github.dockerjava.jaxrs1.command;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * Copy files or folders from a container.
 *
 */
public class CopyFileFromContainerCommand extends AbstrDockerCmd<CopyFileFromContainerCommand, InputStream> implements CopyFileFromContainerCmd {

	private static final Logger LOGGER = LoggerFactory.getLogger(CopyFileFromContainerCommand.class);

	private String containerId;
	
	@JsonProperty("HostPath")
    private String hostPath = ".";

    @JsonProperty("Resource")
    private String resource;

	public CopyFileFromContainerCommand(String containerId, String resource) {
		withContainerId(containerId);
		withResource(resource);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public String getResource() {
        return resource;
    }

    @Override
	public CopyFileFromContainerCommand withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public CopyFileFromContainerCommand withResource(String resource) {
		Preconditions.checkNotNull(resource, "resource was not specified");
		this.resource = resource;
		return this;
	}
	
	@Override
	public String getHostPath() {
		return hostPath;
	}
	
	@Override
	public CopyFileFromContainerCommand withHostPath(String hostPath) {
		Preconditions.checkNotNull(hostPath, "hostPath was not specified");
		this.hostPath = hostPath;
		return this;
	}

	@Override
    public String toString() {
        return new ToStringBuilder(this).append("cp ")
            .append(containerId)
            .append(":")
            .append(resource)
            .toString();
    }
    
    /**
     * @throws NotFoundException No such container
     */
	@Override
    public InputStream exec() throws NotFoundException {
    	return super.exec();
    }

	protected InputStream impl() throws DockerException {

		WebResource webResource = baseResource.path(String.format("/containers/%s/copy", containerId));
		
		LOGGER.trace("POST: " + webResource.toString());
		WebResource.Builder builder = webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).type("application/json");

		return builder.post(ClientResponse.class, this).getEntityInputStream();
	}


}
