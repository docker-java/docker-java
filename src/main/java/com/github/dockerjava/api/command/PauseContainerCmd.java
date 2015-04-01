package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Pause a container.
 *
 * @param containerId - Id of the container
 *
 */
public interface PauseContainerCmd extends DockerCmd<Void>{

	public String getContainerId();

	public PauseContainerCmd withContainerId(String containerId);

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public Void exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<PauseContainerCmd, Void> {
	}

}