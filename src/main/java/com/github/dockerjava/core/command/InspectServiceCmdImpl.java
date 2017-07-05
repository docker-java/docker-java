package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.InspectServiceCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Service;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(serviceId, "serviceId was not specified");
        this.serviceId = serviceId;
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
