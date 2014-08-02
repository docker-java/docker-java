package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

/**
 *
 * Copy files or folders from a container.
 *
 */
public class CopyFileFromContainerCmd extends AbstrDockerCmd<CopyFileFromContainerCmd, ClientResponse> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CopyFileFromContainerCmd.class);

	private String containerId, resource;

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

    @Override
    public String toString() {
        return new StringBuilder("cp ")
            .append(containerId)
            .append(":")
            .append(resource)
            .toString();
    }

	protected ClientResponse impl() throws DockerException {
		CopyConfig copyConfig = new CopyConfig();
		copyConfig.setResource(resource);

		WebResource webResource =
				baseResource.path(String.format("/containers/%s/copy", containerId));

		try {
			LOGGER.trace("POST: " + webResource.toString());
			WebResource.Builder builder =
					webResource.accept(MediaType.APPLICATION_OCTET_STREAM_TYPE).type("application/json");

			return builder.post(ClientResponse.class, copyConfig);
		} catch (UniformInterfaceException exception) {
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

    /**
     * Configuration object for copy command.
     * @author Victor Lyuboslavsky
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CopyConfig {

        @JsonProperty("HostPath")
        private String hostPath;

        @JsonProperty("Resource")
        private String resource;

        /**
         * Constructor.
         */
        public CopyConfig() {
            hostPath = ".";
        }

        /**
         * Retrieves the 'resource' variable.
         * @return the 'resource' variable value
         */
        public String getResource() {
            return resource;
        }

        /**
         * Sets the 'resource' variable.
         * @param resource the new 'resource' variable value to set
         */
        public void setResource(String resource) {
            this.resource = resource;
        }

        /**
         * Retrieves the 'hostPath' variable.
         * @return the 'hostPath' variable value
         */
        public String getHostPath() {
            return hostPath;
        }

        /**
         * Sets the 'hostPath' variable.
         * @param hostPath the new 'hostPath' variable value to set
         */
        public void setHostPath(String hostPath) {
            this.hostPath = hostPath;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

    }
}
