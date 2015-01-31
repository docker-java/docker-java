package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.KillContainerCmd;


/**
 * Kill a running container.
 */
public class KillContainerCmdImpl extends AbstrDockerCmd<KillContainerCmd, Void> implements KillContainerCmd {

	private String containerId, signal;

	public KillContainerCmdImpl(KillContainerCmd.Exec exec, String containerId) {
		super(exec);
		withContainerId(containerId);
	}

    @Override
	public String getContainerId() {
        return containerId;
    }

    @Override
	public String getSignal() {
        return signal;
    }

    @Override
	public KillContainerCmd withContainerId(String containerId) {
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
		return this;
	}

	@Override
	public KillContainerCmd withSignal(String signal) {
		checkNotNull(signal, "signal was not specified");
		this.signal = signal;
		return this;
	}

    @Override
    public String toString() {
        return "kill " + containerId;
    }
    
    /**
     * @throws NotFoundException No such container
     */
	@Override
    public Void exec() throws NotFoundException {
    	return super.exec();
    }
}
