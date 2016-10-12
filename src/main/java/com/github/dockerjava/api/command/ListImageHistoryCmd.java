package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ImageHistory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;

/**
 * List history of an image.
 */
public interface ListImageHistoryCmd extends SyncDockerCmd<List<ImageHistory>> {

    @CheckForNull
    String getImageId();

    ListImageHistoryCmd withImageId(@Nonnull String imageId);

    /**
     * @throws NotFoundException
     *             No such image
     */
    @Override
    List<ImageHistory> exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ListImageHistoryCmd, List<ImageHistory>> {
    }

}
