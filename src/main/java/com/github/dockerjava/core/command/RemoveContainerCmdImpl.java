package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.RemoveContainerCmd;

/**
 * Remove a container.
 * 
 * @param removeVolumes - true or false, Remove the volumes associated to the container. Defaults to false
 * @param force - true or false, Removes the container even if it was running. Defaults to false
 */
public class RemoveContainerCmdImpl extends	AbstrDockerCmd<RemoveContainerCmd, Void> implements RemoveContainerCmd {

	private String containerId;

	private boolean removeVolumes, force;

	public RemoveContainerCmdImpl(RemoveContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

	@Override
	public String getContainerId() {
		return containerId;
	}

	@Override
	public boolean hasRemoveVolumesEnabled() {
		return removeVolumes;
	}

	@Override
	public boolean hasForceEnabled() {
		return force;
	}

	@Override
	public RemoveContainerCmd withContainerId(String containerId) {
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public RemoveContainerCmd withRemoveVolumes(boolean removeVolumes) {
		this.removeVolumes = removeVolumes;
		return this;
	}

	@Override
	public RemoveContainerCmd withForce() {
		return withForce(true);
	}

	@Override
	public RemoveContainerCmd withForce(boolean force) {
		this.force = force;
		return this;
	}

	@Override
	public String toString() {
		return new StringBuilder("rm ")
				.append(removeVolumes ? "--volumes=true" : "")
				.append(force ? "--force=true" : "").append(containerId)
				.toString();
	}

	/**
	 * @throws NotFoundException No such container
	 */
	@Override
	public Void exec() throws NotFoundException {
		return super.exec();
	}
}
