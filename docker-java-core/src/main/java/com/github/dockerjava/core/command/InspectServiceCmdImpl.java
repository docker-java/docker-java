package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.InspectServiceCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Service;

/**
 * Inspect the details of a container.
 */
public class InspectServiceCmdImpl extends AbstrDockerCmd<InspectServiceCmd, Service> implements
        InspectServiceCmd {

    private String serviceId;

    public InspectServiceCmdImpl(Exec exec, String serviceId) {
        super(exec);
        withServiceId(serviceId);
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public InspectServiceCmd withServiceId(String serviceId) {
        this.serviceId = Objects.requireNonNull(serviceId, "serviceId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such service
     */
    @Override
    public Service exec() throws NotFoundException {
        return super.exec();
    }
}
