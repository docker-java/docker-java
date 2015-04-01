package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Kill a running container.
 */
public interface KillContainerCmd extends DockerCmd<Void> {

	public String getContainerId();

	public String getSignal();

	public KillContainerCmd withContainerId(String containerId);

	public KillContainerCmd withSignal(String signal);

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public Void exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<KillContainerCmd, Void> {
	}

}