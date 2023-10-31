package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Objects;

import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;

/**
 * Restart a running container.
 *
 * @param signal  - Signal to send to the container as an integer or string (e.g. SIGINT).
 * @param timeout - Timeout in seconds before killing the container. Defaults to 10 seconds.
 */
public class RestartContainerCmdImpl extends AbstrDockerCmd<RestartContainerCmd, Void> implements RestartContainerCmd {

    private String containerId;

    private Integer timeout = 10;

    private String signal;

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

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_42}
     */
    @CheckForNull
    @Override
    public String getSignal() {
        return signal;
    }

    @Override
    public RestartContainerCmd withContainerId(String containerId) {
        Objects.requireNonNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public RestartContainerCmd withTimeout(Integer timeout) {
        Objects.requireNonNull(timeout, "timeout was not specified");
        checkArgument(timeout >= 0, "timeout must be greater or equal 0");
        this.timeout = timeout;
        return this;
    }

    @Override
    public RestartContainerCmd withSignal(String signal) {
        Objects.requireNonNull(signal, "signal was not specified");
        this.signal = signal;
        return this;
    }

    /**
     * @throws NotFoundException No such container
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
