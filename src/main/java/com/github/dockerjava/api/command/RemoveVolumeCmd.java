package com.github.dockerjava.api.command;

import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Remove a volume.
 *
 * @author Marcus Linke
 */
public interface RemoveVolumeCmd extends SyncDockerCmd<Void> {

    public String getName();

    public RemoveVolumeCmd withName(@Nonnull String name);

    /**
     * @throws NotFoundException
     *             No such volume
     * @throws ConflictException
     *             Volume is in use and cannot be removed
     */
    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<RemoveVolumeCmd, Void> {
    }
}
