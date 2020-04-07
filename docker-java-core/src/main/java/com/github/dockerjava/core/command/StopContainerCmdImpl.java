package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.StopContainerCmd;
import com.github.dockerjava.api.command.StopContainerSpec;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;

/**
 * Stop a running container.
 *
 * @param containerId
 *            - Id of the container
 * @param timeout
 *            - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class StopContainerCmdImpl extends AbstrDockerCmd<StopContainerCmd, Void> implements StopContainerCmd {

    private StopContainerSpec spec;

    public StopContainerCmdImpl(StopContainerCmd.Exec exec, String containerId) {
        this(exec, StopContainerSpec.of(containerId));
    }

    StopContainerCmdImpl(StopContainerCmd.Exec exec, StopContainerSpec spec) {
        super(exec);
        this.spec = spec;
    }

    @Override
    public String getContainerId() {
        return spec.getContainerId();
    }

    @Override
    public Integer getTimeout() {
        return spec.getTimeout();
    }

    @Override
    public StopContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.spec = spec.withContainerId(containerId);
        return this;
    }

    @Override
    public StopContainerCmd withTimeout(Integer timeout) {
        checkNotNull(timeout, "timeout was not specified");
        checkArgument(timeout >= 0, "timeout must be greater or equal 0");
        this.spec = spec.withTimeout(timeout);
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     * @throws NotModifiedException
     *             Container already stopped
     */
    @Override
    public Void exec() throws NotFoundException, NotModifiedException {
        return super.exec();
    }
}
