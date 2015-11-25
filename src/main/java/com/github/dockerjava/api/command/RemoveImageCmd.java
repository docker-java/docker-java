package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 *
 * Remove an image, deleting any tags it might have.
 *
 */
public interface RemoveImageCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    public String getImageId();

    @CheckForNull
    public Boolean hasForceEnabled();

    @CheckForNull
    public Boolean hasNoPruneEnabled();

    public RemoveImageCmd withImageId(@Nonnull String imageId);

    /**
     * force parameter to force delete of an image, even if it's tagged in multiple repositories
     */
    public RemoveImageCmd withForce(Boolean force);

    /**
     * noprune parameter to prevent the deletion of parent images
     *
     */
    public RemoveImageCmd withNoPrune(Boolean noPrune);

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public Void exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<RemoveImageCmd, Void> {
    }

}