package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Remove a container.
 *
 * @param removeVolumes
 *            - true or false, Remove the volumes associated to the container. Defaults to false
 * @param force
 *            - true or false, Removes the container even if it was running. Defaults to false
 */
public interface RemoveContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    Boolean hasRemoveVolumesEnabled();

    @CheckForNull
    Boolean hasForceEnabled();

    RemoveContainerCmd withContainerId(@Nonnull String containerId);

    RemoveContainerCmd withRemoveVolumes(Boolean removeVolumes);

    RemoveContainerCmd withForce(Boolean force);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveContainerCmd, Void> {
    }

}
