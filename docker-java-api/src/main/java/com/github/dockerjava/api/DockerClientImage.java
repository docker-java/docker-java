package com.github.dockerjava.api;

import com.github.dockerjava.api.command.CreateImageCmd;
import com.github.dockerjava.api.command.InspectImageCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.LoadImageCmd;
import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.command.PushImageCmd;
import com.github.dockerjava.api.command.RemoveImageCmd;
import com.github.dockerjava.api.command.SaveImageCmd;
import com.github.dockerjava.api.command.SaveImagesCmd;
import com.github.dockerjava.api.command.SearchImagesCmd;
import com.github.dockerjava.api.model.Identifier;

import javax.annotation.Nonnull;
import java.io.InputStream;

public interface DockerClientImage {
    /**
     * * IMAGE API *
     */

    PullImageCmd pullImageCmd(@Nonnull String repository);

    PushImageCmd pushImageCmd(@Nonnull String name);

    PushImageCmd pushImageCmd(@Nonnull Identifier identifier);

    CreateImageCmd createImageCmd(@Nonnull String repository, @Nonnull InputStream imageStream);

    /**
     * Loads a tarball with a set of images and tags into a Docker repository.
     * <p>
     * Corresponds to POST /images/load API endpoint.
     *
     * @param imageStream stream of the tarball file
     * @return created command
     * @since {@link RemoteApiVersion#VERSION_1_7}
     */
    LoadImageCmd loadImageCmd(@Nonnull InputStream imageStream);

    SearchImagesCmd searchImagesCmd(@Nonnull String term);

    RemoveImageCmd removeImageCmd(@Nonnull String imageId);

    ListImagesCmd listImagesCmd();

    InspectImageCmd inspectImageCmd(@Nonnull String imageId);

    /**
     * @param name The name, e.g. "alexec/busybox" or just "busybox" if you want to default. Not null.
     */
    SaveImageCmd saveImageCmd(@Nonnull String name);

    /**
     * Command to download multiple images at once.
     *
     * @return command (builder)
     */
    SaveImagesCmd saveImagesCmd();
}
