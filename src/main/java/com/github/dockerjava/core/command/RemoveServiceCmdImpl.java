package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RemoveServiceCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(serviceId, "serviceId was not specified");
        this.serviceId = serviceId;
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
