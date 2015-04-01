package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

public interface InspectContainerCmd extends DockerCmd<InspectContainerResponse> {

	public String getContainerId();

	public InspectContainerCmd withContainerId(String containerId);

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public InspectContainerResponse exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<InspectContainerCmd, InspectContainerResponse> {
	}

}