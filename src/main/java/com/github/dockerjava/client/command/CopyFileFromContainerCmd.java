package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.entity;

/**
 *
 * Copy files or folders from a container.
 *
 */
public class CopyFileFromContainerCmd extends AbstrDockerCmd<CopyFileFromContainerCmd, Response> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CopyFileFromContainerCmd.class);

	private String containerId;
	
	@JsonProperty("HostPath")
    private String hostPath = ".";

    @JsonProperty("Resource")
    private String resource;

	public CopyFileFromContainerCmd(String containerId, String resource) {
		withContainerId(containerId);
		withResource(resource);
	}

    public String getContainerId() {
        return containerId;
    }

    public String getResource() {
        return resource;
    }

    public CopyFileFromContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	public CopyFileFromContainerCmd withResource(String resource) {
		Preconditions.checkNotNull(resource, "resource was not specified");
		this.resource = resource;
		return this;
	}
	
	public String getHostPath() {
		return hostPath;
	}
	
	public CopyFileFromContainerCmd withHostPath(String hostPath) {
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

	protected Response impl() throws DockerException {

		WebTarget webResource =
				baseResource.path("/containers/{id}/copy").resolveTemplate("id", containerId);

		try {
			LOGGER.trace("POST: " + webResource.toString());
			Invocation.Builder builder =
					webResource.request().accept(MediaType.APPLICATION_OCTET_STREAM_TYPE);

			return builder.post(entity(this, MediaType.APPLICATION_JSON), Response.class);
		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 400) {
				throw new DockerException("bad parameter");
			} else if (exception.getResponse().getStatus() == 404) {
				throw new DockerException(String.format("No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 500) {
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}
	}


}
