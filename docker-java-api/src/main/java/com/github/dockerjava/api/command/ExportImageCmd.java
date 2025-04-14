package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.Nonnull;

import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Command to export a Docker image as a tarball.
 */
public interface ExportImageCmd extends SyncDockerCmd<InputStream> {

    /**
     * Set the image ID or name to export.
     *
     * @param imageId the image ID or name
     * @return this command
     */
    ExportImageCmd withImageId(@Nonnull String imageId);

    /**
     * Get the image ID or name to export.
     *
     * @return the image ID or name
     */
    String getImageId();

    /**
     * Execute the command.
     *
     * @return an InputStream of the tarball
     * @throws NotFoundException if the image does not exist
     */
    @Override
    InputStream exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ExportImageCmd, InputStream> {
    }
}