package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.WaitContainerCmd;
import com.github.dockerjava.api.model.WaitResponse;

/**
 * Wait a container
 *
 * Block until container stops, then returns its exit code
 */
public class WaitContainerCmdImpl extends AbstrAsyncDockerCmd<WaitContainerCmd, WaitResponse> implements
        WaitContainerCmd {

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
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

}
