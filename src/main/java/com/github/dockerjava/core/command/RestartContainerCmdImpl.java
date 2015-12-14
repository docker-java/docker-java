package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Restart a running container.
 *
 * @param timeout
 *            - Timeout in seconds before killing the container. Defaults to 10 seconds.
 *
 */
public class RestartContainerCmdImpl extends AbstrDockerCmd<RestartContainerCmd, Void> implements RestartContainerCmd {

    private String containerId;

    private Integer timeout = 10;

    public RestartContainerCmdImpl(RestartContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public Integer getTimeout() {
        return timeout;
    }

    @Override
    public RestartContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public RestartContainerCmd withtTimeout(Integer timeout) {
        checkNotNull(timeout, "timeout was not specified");
        checkArgument(timeout >= 0, "timeout must be greater or equal 0");
        this.timeout = timeout;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
