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

    String getName();

    RemoveVolumeCmd withName(@Nonnull String name);

    /**
     * @throws NotFoundException
     *             No such volume
     * @throws ConflictException
     *             Volume is in use and cannot be removed
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveVolumeCmd, Void> {
    }
}
