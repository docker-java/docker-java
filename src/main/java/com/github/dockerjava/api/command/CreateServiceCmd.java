package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;

/**
 * Command to create a new service
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public interface CreateServiceCmd extends SyncDockerCmd<CreateServiceResponse> {

    @CheckForNull
    ServiceSpec getServiceSpec();

    CreateServiceCmd withServiceSpec(ServiceSpec serviceSpec);

    /**
     * @throws ConflictException
     *             Named service already exists
     */
    @Override
    CreateServiceResponse exec() throws ConflictException;

    interface Exec extends DockerCmdSyncExec<CreateServiceCmd, CreateServiceResponse> {
    }

}
