package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Restart a running container.
 *
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public interface RestartContainerCmd extends DockerCmd<Void> {

	public String getContainerId();

	public int getTimeout();

	public RestartContainerCmd withContainerId(String containerId);

	public RestartContainerCmd withtTimeout(int timeout);

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public Void exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<RestartContainerCmd, Void> {
	}

}