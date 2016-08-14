package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.LoadImageResponseItem;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

public interface LoadImageCmd extends AsyncDockerCmd<LoadImageCmd, LoadImageResponseItem> {

    @CheckForNull
    InputStream getImageStream();

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    LoadImageCmd withImageStream(@Nonnull InputStream imageStream);

    @CheckForNull
    Boolean getQuiet();

    LoadImageCmd withQuiet(Boolean quiet);

    interface Exec extends DockerCmdAsyncExec<LoadImageCmd, LoadImageResponseItem> {
    }
}
