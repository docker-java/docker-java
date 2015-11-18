package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.NotFoundException;

/**
 * Kill a running container.
 */
public interface KillContainerCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getContainerId();

    @CheckForNull
    public String getSignal();

    public KillContainerCmd withContainerId(@Nonnull String containerId);

    public KillContainerCmd withSignal(String signal);

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<KillContainerCmd, Void> {
    }

}