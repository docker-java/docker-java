package com.github.dockerjava.api.command;

import java.util.List;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.InternalServerErrorException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.ChangeLog;

public interface ContainerDiffCmd extends DockerCmd<List<ChangeLog>> {

	public String getContainerId();

	public ContainerDiffCmd withContainerId(String containerId);

	@Override
	public String toString();

	/**
	 * @throws NotFoundException No such container
	 * @throws InternalServerErrorException server error
	 * @throws DockerException unexpected http status code
	 */
	@Override
	public List<ChangeLog> exec() throws NotFoundException;
	
	public static interface Exec extends DockerCmdExec<ContainerDiffCmd, List<ChangeLog>> {
	}

}