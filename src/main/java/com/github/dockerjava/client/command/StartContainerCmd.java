
package com.github.dockerjava.client.command;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.client.model.*;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.NotFoundException;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * Run a container
 */
public class StartContainerCmd extends AbstrDockerCmd<StartContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartContainerCmd.class);

	private String containerId;

	private StartContainerConfig startContainerConfig;

	public StartContainerCmd(String containerId) {
		startContainerConfig = new StartContainerConfig();
		withContainerId(containerId);
	}

    public String getContainerId() {
        return containerId;
    }

    public StartContainerCmd withBinds(Bind... binds) {
		startContainerConfig.setBinds(binds);
		return this;
	}

	public StartContainerCmd withLinks(final Link... links)
	{
		startContainerConfig.setLinks(links);
		return this;
	}

	public StartContainerCmd withLxcConf(final LxcConf[] lxcConf)
	{
		startContainerConfig.setLxcConf(lxcConf);
		return this;
	}

	public StartContainerCmd withPortBindings(Ports portBindings) {
		startContainerConfig.setPortBindings(portBindings);
		return this;
	}

	public StartContainerCmd withPrivileged(boolean privileged) {
		startContainerConfig.setPrivileged(privileged);
		return this;
	}

	public StartContainerCmd withPublishAllPorts(boolean publishAllPorts) {
		startContainerConfig.setPublishAllPorts(publishAllPorts);
		return this;
	}

	public StartContainerCmd withDns(String dns) {
		startContainerConfig.setDns(dns);
		return this;
	}


	public StartContainerCmd withVolumesFrom(String volumesFrom) {
		startContainerConfig.setVolumesFrom(volumesFrom);
		return this;
	}

	public StartContainerCmd withContainerId(String containerId) {
		Preconditions.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("run ")
            .append(containerId)
            .append(" using ")
            .append(startContainerConfig)
            .toString();
    }

	protected Void impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format("/containers/%s/start", containerId));

		try {
			LOGGER.trace("POST: {}", webResource);
			Builder builder = webResource.accept(MediaType.APPLICATION_JSON);
			if (startContainerConfig != null) {
				builder.type(MediaType.APPLICATION_JSON).post(startContainerConfig);
			} else {
				builder.post((StartContainerConfig) null);
			}


		} catch (UniformInterfaceException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format("No such container %s", containerId));
			} else if(exception.getResponse().getStatus() == 304) {
				//no error
				LOGGER.warn("Container already started {}", containerId);
			} else if (exception.getResponse().getStatus() == 204) {
				//no error
				LOGGER.trace("Successfully started container {}", containerId);
			} else if (exception.getResponse().getStatus() == 500) {
				LOGGER.error("", exception);
				throw new DockerException("Server error", exception);
			} else {
				throw new DockerException(exception);
			}
		}

		return null;
	}

    /**
     *
     * @author Konstantin Pelykh (kpelykh@gmail.com)
     *
     */
    private static class StartContainerConfig {

        @JsonProperty("Binds")
        private Binds binds = new Binds();

        @JsonProperty("Links")
        private Links links = new Links();

        @JsonProperty("LxcConf")
        private LxcConf[] lxcConf;

        @JsonProperty("PortBindings")
        private Ports portBindings;

        @JsonProperty("PublishAllPorts")
        private boolean publishAllPorts;

        @JsonProperty("Privileged")
        private boolean privileged;

        @JsonProperty("Dns")
        private String dns;

        @JsonProperty("VolumesFrom")
        private String volumesFrom;

        @JsonIgnore
        public Bind[] getBinds() {
            return binds.getBinds();
        }

        @JsonIgnore
        public void setBinds(Bind[] binds) {
            this.binds = new Binds(binds);
        }

        @JsonIgnore
        public Link[] getLinks() {
            return links.getLinks();
        }

        @JsonIgnore
        public void setLinks(Link[] links) {
            this.links = new Links(links);
        }

        public LxcConf[] getLxcConf() {
            return lxcConf;
        }

        public void setLxcConf(LxcConf[] lxcConf) {
            this.lxcConf = lxcConf;
        }

        public Ports getPortBindings() {
            return portBindings;
        }

        public void setPortBindings(Ports portBindings) {
            this.portBindings = portBindings;
        }

        public boolean isPublishAllPorts() {
            return publishAllPorts;
        }

        public void setPublishAllPorts(boolean publishAllPorts) {
            this.publishAllPorts = publishAllPorts;
        }

        public boolean isPrivileged() {
            return privileged;
        }

        public void setPrivileged(boolean privileged) {
            this.privileged = privileged;
        }

        public String getDns() {
            return dns;
        }

        public void setDns(String dns) {
            this.dns = dns;
        }

        public String getVolumesFrom() {
            return volumesFrom;
        }

        public void setVolumesFrom(String volumesFrom) {
            this.volumesFrom = volumesFrom;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
