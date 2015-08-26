package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Remove a container.
 *
 * @param removeVolumes
 *            - true or false, Remove the volumes associated to the container. Defaults to false
 * @param force
 *            - true or false, Removes the container even if it was running. Defaults to false
 */
public interface RemoveContainerCmd extends SyncDockerCmd<Void> {

    public String getContainerId();

    public Boolean hasRemoveVolumesEnabled();

    public Boolean hasForceEnabled();

    public RemoveContainerCmd withContainerId(String containerId);

    public RemoveContainerCmd withRemoveVolumes(Boolean removeVolumes);

    public RemoveContainerCmd withForce();

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