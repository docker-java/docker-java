package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;

/**
 * Inspect the details of a container.
 */
public class InspectContainerCmdImpl extends AbstrDockerCmd<InspectContainerCmd, InspectContainerResponse> implements InspectContainerCmd {

	private String containerId;

	public InspectContainerCmdImpl(InspectContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public InspectContainerCmd withContainerId(String containerId) {
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

    @Override
    public String toString() {
        return "inspect " + containerId;
    }
    
    /**
     * @throws NotFoundException No such container
     */
	@Override
    public InspectContainerResponse exec() throws NotFoundException {
    	return super.exec();
    }
}
