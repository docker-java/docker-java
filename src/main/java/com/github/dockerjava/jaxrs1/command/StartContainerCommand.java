package com.github.dockerjava.jaxrs1.command;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.StartContainerCmd;
import com.github.dockerjava.client.command.AbstrDockerCmd;
import com.github.dockerjava.client.model.Bind;
import com.github.dockerjava.client.model.Binds;
import com.github.dockerjava.client.model.Link;
import com.github.dockerjava.client.model.Links;
import com.github.dockerjava.client.model.LxcConf;
import com.github.dockerjava.client.model.Ports;
import com.google.common.base.Preconditions;
import com.sun.jersey.api.client.WebResource;

/**
 * Start a container
 */
public class StartContainerCommand extends AbstrDockerCmd<StartContainerCommand, Void> implements StartContainerCmd {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(StartContainerCommand.class);

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
	
	public StartContainerCommand(String containerId) {
		withContainerId(containerId);
	}

	@Override
	@JsonIgnore
	public Bind[] getBinds() {
		return binds.getBinds();
	}

	@Override
	@JsonIgnore
	public Link[] getLinks() {
		return links.getLinks();
	}

	@Override
	public LxcConf[] getLxcConf() {
		return lxcConf;
	}

	@Override
	public Ports getPortBindings() {
		return portBindings;
	}

	@Override
	public boolean isPublishAllPorts() {
		return publishAllPorts;
	}

	@Override
	public boolean isPrivileged() {
		return privileged;
	}

	@Override
	public String getDns() {
		return dns;
	}

	@Override
	public String getVolumesFrom() {
		return volumesFrom;
	}

	
	@Override
	public String getContainerId() {
		return containerId;
	}

	@Override
	@JsonIgnore
	public StartContainerCmd withBinds(Bind... binds) {
		Preconditions.checkNotNull(binds, "binds was not specified");
		this.binds = new Binds(binds);
		return this;
	}

	@Override
	@JsonIgnore
	public StartContainerCmd withLinks(Link... links) {
		Preconditions.checkNotNull(links, "links was not specified");
		this.links = new Links(links);
		return this;
	}

	@Override
	public StartContainerCmd withLxcConf(LxcConf... lxcConf) {
		Preconditions.checkNotNull(lxcConf, "lxcConf was not specified");
		this.lxcConf = lxcConf;
		return this;
	}

	@Override
	public StartContainerCmd withPortBindings(Ports portBindings) {
		Preconditions.checkNotNull(portBindings,
				"portBindings was not specified");
		this.portBindings = portBindings;
		return this;
	}

	@Override
	public StartContainerCmd withPrivileged(boolean privileged) {
		this.privileged = privileged;
		return this;
	}

	@Override
	public StartContainerCmd withPublishAllPorts(boolean publishAllPorts) {
		this.publishAllPorts = publishAllPorts;
		return this;
	}

	@Override
	public StartContainerCmd withDns(String dns) {
		Preconditions.checkNotNull(dns, "dns was not specified");
		this.dns = dns;
		return this;
	}

	@Override
	public StartContainerCmd withVolumesFrom(String volumesFrom) {
		Preconditions
				.checkNotNull(volumesFrom, "volumesFrom was not specified");
		this.volumesFrom = volumesFrom;
		return this;
	}

	@Override
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
	
	/**
	 * @throws NotFoundException No such container
	 * @throws NotModifiedException Container already started
	 */
	@Override
	public Void exec() throws NotFoundException, NotModifiedException {
		return super.exec();
	}

	protected Void impl() throws DockerException {
		WebResource webResource = baseResource.path(String.format(
				"/containers/%s/start", containerId));
		
		LOGGER.trace("POST: {}", webResource);
		webResource.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).post(this);
		
		return null;
	}

	
}
