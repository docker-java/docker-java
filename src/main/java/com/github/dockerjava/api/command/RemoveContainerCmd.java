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
    public String getContainerId();

    @CheckForNull
    public Boolean hasRemoveVolumesEnabled();

    @CheckForNull
    public Boolean hasForceEnabled();

    public RemoveContainerCmd withContainerId(@Nonnull String containerId);

    public RemoveContainerCmd withRemoveVolumes(Boolean removeVolumes);

    public RemoveContainerCmd withForce(Boolean force);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<RemoveContainerCmd, Void> {
    }

}