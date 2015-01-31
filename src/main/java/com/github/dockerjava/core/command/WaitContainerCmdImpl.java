package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.WaitContainerCmd;

/**
 * Wait a container
 * 
 * Block until container stops, then returns its exit code
 */
public class WaitContainerCmdImpl extends AbstrDockerCmd<WaitContainerCmd, Integer> implements WaitContainerCmd {

	private String containerId;

	public WaitContainerCmdImpl(WaitContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public WaitContainerCmd withContainerId(String containerId) {
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

    @Override
    public String toString() {
        return "wait " + containerId;
    }
}
