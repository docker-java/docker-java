package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.TopContainerCmd;
import com.github.dockerjava.api.command.TopContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * List processes running inside a container
 */
public class TopContainerCmdImpl extends AbstrDockerCmd<TopContainerCmd, TopContainerResponse> implements
        TopContainerCmd {

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
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public TopContainerCmd withPsArgs(String psArgs) {
        this.psArgs = Objects.requireNonNull(psArgs, "psArgs was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public TopContainerResponse exec() throws NotFoundException {
        return super.exec();
    }
}
