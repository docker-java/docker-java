package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;

/**
 * Inspect the details of an image.
 */
public interface InspectImageCmd extends SyncDockerCmd<InspectImageResponse> {

    public String getImageId();

    public InspectImageCmd withImageId(String imageId);

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    public InspectImageResponse exec() throws NotFoundException;

    public static interface Exec extends DockerCmdSyncExec<InspectImageCmd, InspectImageResponse> {
    }

}