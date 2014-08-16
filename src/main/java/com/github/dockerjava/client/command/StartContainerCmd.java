package com.github.dockerjava.client.command;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Invocation;
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
import javax.ws.rs.client.WebTarget;

import static javax.ws.rs.client.Entity.entity;

/**
 * Run a container
 */
public class StartContainerCmd extends AbstrDockerCmd<StartContainerCmd, Void> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StartContainerCmd.class);

	private String containerId;

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
	
	public StartContainerCmd(String containerId) {
		withContainerId(containerId);
	}

	@JsonIgnore
	public Bind[] getBinds() {
		return binds.getBinds();
	}

	@JsonIgnore
	public Link[] getLinks() {
		return links.getLinks();
	}

	public LxcConf[] getLxcConf() {
		return lxcConf;
	}

	public Ports getPortBindings() {
		return portBindings;
	}

	public boolean isPublishAllPorts() {
		return publishAllPorts;
	}

	public boolean isPrivileged() {
		return privileged;
	}

	public String getDns() {
		return dns;
	}

	public String getVolumesFrom() {
		return volumesFrom;
	}

	
	public String getContainerId() {
		return containerId;
	}

	@JsonIgnore
	public StartContainerCmd withBinds(Bind... binds) {
		Preconditions.checkNotNull(binds, "binds was not specified");
		this.binds = new Binds(binds);
		return this;
	}

	@JsonIgnore
	public StartContainerCmd withLinks(Link... links) {
		Preconditions.checkNotNull(links, "links was not specified");
		this.links = new Links(links);
		return this;
	}

	public StartContainerCmd withLxcConf(LxcConf... lxcConf) {
		Preconditions.checkNotNull(lxcConf, "lxcConf was not specified");
		this.lxcConf = lxcConf;
		return this;
	}

	public StartContainerCmd withPortBindings(Ports portBindings) {
		Preconditions.checkNotNull(portBindings,
				"portBindings was not specified");
		this.portBindings = portBindings;
		return this;
	}

	public StartContainerCmd withPrivileged(boolean privileged) {
		this.privileged = privileged;
		return this;
	}

	public StartContainerCmd withPublishAllPorts(boolean publishAllPorts) {
		this.publishAllPorts = publishAllPorts;
		return this;
	}

	public StartContainerCmd withDns(String dns) {
		Preconditions.checkNotNull(dns, "dns was not specified");
		this.dns = dns;
		return this;
	}

	public StartContainerCmd withVolumesFrom(String volumesFrom) {
		Preconditions
				.checkNotNull(volumesFrom, "volumesFrom was not specified");
		this.volumesFrom = volumesFrom;
		return this;
	}

	public StartContainerCmd withContainerId(String containerId) {
		Preconditions
				.checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this).toString();
	}

	protected Void impl() throws DockerException {
		WebTarget webResource = baseResource.path(
				"/containers/{id}/start").resolveTemplate("id", containerId);

		try {
			LOGGER.trace("POST: {}", webResource);
			Invocation.Builder builder = webResource.request().accept(MediaType.APPLICATION_JSON);

			builder.post(entity(this, MediaType.APPLICATION_JSON));

		} catch (ClientErrorException exception) {
			if (exception.getResponse().getStatus() == 404) {
				throw new NotFoundException(String.format(
						"No such container %s", containerId));
			} else if (exception.getResponse().getStatus() == 304) {
				// no error
				LOGGER.warn("Container already started {}", containerId);
			} else if (exception.getResponse().getStatus() == 204) {
				// no error
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

	
}
