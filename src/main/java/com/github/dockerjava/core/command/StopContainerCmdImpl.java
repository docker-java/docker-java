package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkArgument;
import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.NotModifiedException;
import com.github.dockerjava.api.command.StopContainerCmd;

/**
 * Stop a running container.
 *
 * @param containerId - Id of the container
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class StopContainerCmdImpl extends AbstrDockerCmd<StopContainerCmd, Void> implements StopContainerCmd {

	private String containerId;

	private int timeout = 10;

	public StopContainerCmdImpl(StopContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public int getTimeout() {
        return timeout;
    }

    @Override
	public StopContainerCmd withContainerId(String containerId) {
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public StopContainerCmd withTimeout(int timeout) {
		checkArgument(timeout >= 0, "timeout must be greater or equal 0");
		this.timeout = timeout;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("stop ")
            .append("--time=" + timeout + " ")
            .append(containerId)
            .toString();
    }
    
    /**
	 * @throws NotFoundException No such container
	 * @throws NotModifiedException Container already stopped
	 */
	@Override
	public Void exec() throws NotFoundException, NotModifiedException {
		return super.exec();
	}
}
