package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 *
 * Remove an image, deleting any tags it might have.
 *
 */
public interface RemoveImageCmd extends SyncDockerCmd<Void> {

    public String getImageId();

    public Boolean hasForceEnabled();

    public Boolean hasNoPruneEnabled();

    public RemoveImageCmd withImageId(String imageId);

    /**
     * force delete of an image, even if it's tagged in multiple repositories
     */
    public RemoveImageCmd withForce();

    /**
     * force parameter to force delete of an image, even if it's tagged in multiple repositories
     */
    public RemoveImageCmd withForce(Boolean force);

    /**
     * prevent the deletion of parent images
     */
    public RemoveImageCmd withNoPrune();

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