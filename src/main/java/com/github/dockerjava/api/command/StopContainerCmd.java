package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;

/**
 * Stop a running container.
 *
 * @param containerId - Id of the container
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public interface StopContainerCmd extends DockerCmd<Void> {

	public String getContainerId();

	public int getTimeout();

	public StopContainerCmd withContainerId(String containerId);

	public StopContainerCmd withTimeout(int timeout);

	/**
	 * @throws NotFoundException No such container
	 * @throws NotModifiedException Container already stopped
	 */
	@Override
	public Void exec() throws NotFoundException, NotModifiedException;
	
	public static interface Exec extends DockerCmdExec<StopContainerCmd, Void> {
	}

}