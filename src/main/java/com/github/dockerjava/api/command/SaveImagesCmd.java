package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;

public interface SaveImagesCmd extends SyncDockerCmd<InputStream> {

    //CHECKSTYLE:OFF

    @CheckForNull
    List<String> getImages();

    /**
     * @param images
     *            The image name, with or wihout tag, e.g. "mysql", "mysql:8.0",
     *            "mysql/mysql-server" or "mysql/mysql-server:8.0". Not null.
     */
    SaveImagesCmd withImages(@Nonnull List<String> images);

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException
     *             One or more images are missing
     */
    @Override
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<SaveImagesCmd, InputStream> {
    }

}
