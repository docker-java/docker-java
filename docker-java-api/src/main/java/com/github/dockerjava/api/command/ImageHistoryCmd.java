package com.github.dockerjava.api.command;

import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ImageHistory;

/**
 * Get the history of an image.
 */
public interface ImageHistoryCmd extends SyncDockerCmd<List<ImageHistory>> {

    @CheckForNull
    String getImageId();

    ImageHistoryCmd withImageId(@Nonnull String imageId);

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    List<ImageHistory> exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ImageHistoryCmd, List<ImageHistory>> {
    }

}
