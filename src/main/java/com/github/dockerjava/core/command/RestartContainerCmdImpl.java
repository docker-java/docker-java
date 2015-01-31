package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkArgument;
import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.RestartContainerCmd;

/**
 * Restart a running container.
 *
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class RestartContainerCmdImpl extends AbstrDockerCmd<RestartContainerCmd, Void> implements RestartContainerCmd {

	private String containerId;

	private int timeout = 10;

	public RestartContainerCmdImpl(RestartContainerCmd.Exec exec, String containerId) {
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
	public RestartContainerCmd withContainerId(String containerId) {
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public RestartContainerCmd withtTimeout(int timeout) {
		checkArgument(timeout >= 0, "timeout must be greater or equal 0");
		this.timeout = timeout;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("restart ")
            .append("--time=" + timeout + " ")
            .append(containerId)
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
