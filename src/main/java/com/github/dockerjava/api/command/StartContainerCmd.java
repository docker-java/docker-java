package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.client.model.Bind;
import com.github.dockerjava.client.model.Link;
import com.github.dockerjava.client.model.LxcConf;
import com.github.dockerjava.client.model.Ports;

/**
 * Start a container
 */
public interface StartContainerCmd extends DockerCmd<Void> {

	public Bind[] getBinds();

	public Link[] getLinks();

	public LxcConf[] getLxcConf();

	public Ports getPortBindings();

	public boolean isPublishAllPorts();

	public boolean isPrivileged();

	public String getDns();

	public String getVolumesFrom();

	public String getContainerId();

	public StartContainerCmd withBinds(Bind... binds);

	public StartContainerCmd withLinks(Link... links);

	public StartContainerCmd withLxcConf(LxcConf... lxcConf);

	public StartContainerCmd withPortBindings(Ports portBindings);

	public StartContainerCmd withPrivileged(boolean privileged);

	public StartContainerCmd withPublishAllPorts(boolean publishAllPorts);

	public StartContainerCmd withDns(String dns);

	public StartContainerCmd withVolumesFrom(String volumesFrom);

	public StartContainerCmd withContainerId(String containerId);

	/**
	 * @throws NotFoundException No such container
	 * @throws NotModifiedException Container already started
	 */
	public Void exec() throws NotFoundException, NotModifiedException;

}