package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.TopContainerResponse;

/**
 * List processes running inside a container
 */
public class TopContainerCmdImpl extends AbstrDockerCmd<TopContainerCmd, TopContainerResponse> implements TopContainerCmd {

	private String containerId;

	private String psArgs;

	public TopContainerCmdImpl(TopContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public String getPsArgs() {
        return psArgs;
    }

    @Override
	public TopContainerCmd withContainerId(String containerId) {
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}


	@Override
	public TopContainerCmd withPsArgs(String psArgs) {
		checkNotNull(psArgs, "psArgs was not specified");
		this.psArgs = psArgs;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("top ")
            .append(containerId)
            .append(psArgs != null ? " " + psArgs : "")
            .toString();
    }

    /**
     * @throws NotFoundException No such container
     */
	@Override
    public TopContainerResponse exec() throws NotFoundException {
    	return super.exec();
    }
}
