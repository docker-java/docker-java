package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * List processes running inside a container
 */
public interface TopContainerCmd extends DockerCmd<TopContainerResponse> {

	public String getContainerId();

	public String getPsArgs();

	public TopContainerCmd withContainerId(String containerId);

	public TopContainerCmd withPsArgs(String psArgs);

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public TopContainerResponse exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<TopContainerCmd, TopContainerResponse> {
	}

}