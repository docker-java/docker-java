package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface LoadImageCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    InputStream getImageStream();

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    LoadImageCmd withImageStream(@Nonnull InputStream imageStream);

    interface Exec extends DockerCmdSyncExec<LoadImageCmd, Void> {
    }
}
