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
    String getImageId();

    @CheckForNull
    Boolean hasForceEnabled();

    @CheckForNull
    Boolean hasNoPruneEnabled();

    RemoveImageCmd withImageId(@Nonnull String imageId);

    /**
     * force parameter to force delete of an image, even if it's tagged in multiple repositories
     */
    RemoveImageCmd withForce(Boolean force);

    /**
     * noprune parameter to prevent the deletion of parent images
     *
     */
    RemoveImageCmd withNoPrune(Boolean noPrune);

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    Void exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<RemoveImageCmd, Void> {
    }

}
