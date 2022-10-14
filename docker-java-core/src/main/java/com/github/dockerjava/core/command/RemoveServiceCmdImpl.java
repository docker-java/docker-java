package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RemoveServiceCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import java.util.Objects;

/**
 * Remove a service.
 */
public class RemoveServiceCmdImpl extends AbstrDockerCmd<RemoveServiceCmd, Void> implements RemoveServiceCmd {

    private String serviceId;

    public RemoveServiceCmdImpl(Exec exec, String serviceId) {
        super(exec);
        withServiceId(serviceId);
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public RemoveServiceCmd withServiceId(String serviceId) {
        this.serviceId = Objects.requireNonNull(serviceId, "serviceId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such service
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
