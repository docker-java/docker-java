package com.github.dockerjava.api.command;

import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

@DockerCommand
public interface InspectContainerCmd extends InspectContainer, SyncDockerCmd<InspectContainerResponse> {

    default InspectContainerCmd fromSpec(InspectContainerSpec spec) {
        return this
                .withContainerId(spec.getContainerId())
                .withSize(spec.getSize());
    }

    InspectContainerCmd withContainerId(@Nonnull String containerId);

    InspectContainerCmd withSize(Boolean showSize);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    InspectContainerResponse exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<InspectContainerCmd, InspectContainerResponse> {
    }
}
