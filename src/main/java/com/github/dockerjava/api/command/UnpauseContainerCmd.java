package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Unpause a container.
 *
 * @param containerId - Id of the container
 *
 */
public interface UnpauseContainerCmd extends DockerCmd<Void> {

	public String getContainerId();

	public UnpauseContainerCmd withContainerId(String containerId);

	/**
	 * @throws NotFoundException No such container
	 */
	public Void exec() throws NotFoundException;

}