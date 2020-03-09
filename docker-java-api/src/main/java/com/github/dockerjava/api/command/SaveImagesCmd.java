package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.util.List;

/** Command for downloading multiple images at once. */
public interface SaveImagesCmd extends SyncDockerCmd<InputStream> {

    /** Image name and tag. */
    interface TaggedImage {

        /**
         * The (tagged) image name.
         * @return "name:tag" if a tag was specified, otherwise "name"
         */
        String asString();
    }

    /**
     * Adds an image to the list of images to download.
     * @param name image name (not null)
     * @param tag tag
     * @return this
     */
    SaveImagesCmd withImage(@Nonnull String name, @Nonnull String tag);


    /**
     * Gets the images that were added by {@link #withImage(String, String)}.
     * @return images to be downloaded
     */
    List<TaggedImage> getImages();

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @throws NotFoundException no such image
     */
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<SaveImagesCmd, InputStream> {
    }

}
