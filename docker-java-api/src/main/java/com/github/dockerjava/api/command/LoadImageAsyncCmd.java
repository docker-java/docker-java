package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.LoadImageAsyncResponseItem;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.InputStream;

/**
 * load image async and return image id or tag
 */
public interface LoadImageAsyncCmd extends AsyncDockerCmd<LoadImageAsyncCmd, LoadImageAsyncResponseItem> {

    @CheckForNull
    InputStream getImageStream();

    /**
     * @param imageStream
     *            the InputStream of the tar file
     */
    LoadImageAsyncCmd withImageStream(@Nonnull InputStream imageStream);

    interface Exec extends DockerCmdAsyncExec<LoadImageAsyncCmd, LoadImageAsyncResponseItem> {
    }

    @Override
    default LoadImageAsyncResultCallback start() {
        return exec(new LoadImageAsyncResultCallback());
    }
}
