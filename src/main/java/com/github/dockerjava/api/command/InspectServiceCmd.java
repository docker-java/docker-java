package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Service;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface InspectServiceCmd extends SyncDockerCmd<Service> {

    @CheckForNull
    String getServiceId();

    InspectServiceCmd withServiceId(@Nonnull String serviceId);

    /**
     * @throws NotFoundException
     *             No such service
     */
    @Override
    Service exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<InspectServiceCmd, Service> {
    }
}
