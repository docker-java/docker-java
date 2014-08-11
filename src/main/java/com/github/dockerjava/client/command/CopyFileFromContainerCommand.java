package com.github.dockerjava.client.command;

import static javax.ws.rs.client.Entity.entity;

import java.io.InputStream;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CopyFileFromContainerCmd;
import com.google.common.base.Preconditions;


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

		WebTarget webResource =
				baseResource.path("/containers/{id}/copy").resolveTemplate("id", containerId);

		LOGGER.trace("POST: " + webResource.toString());
		Invocation.Builder builder =
				webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);

		return builder.post(entity(this, MediaType.APPLICATION_JSON), Response.class).readEntity(InputStream.class);		
	}


}
