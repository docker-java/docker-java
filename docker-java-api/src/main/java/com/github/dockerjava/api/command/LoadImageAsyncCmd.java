package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.LoadResponseItem;

import java.io.InputStream;

public interface LoadImageAsyncCmd extends AsyncDockerCmd<LoadImageAsyncCmd, LoadResponseItem> {
    InputStream getImageStream();

    /**
     * @param imageStream the InputStream of the tar file
     */
    LoadImageAsyncCmd withImageStream(InputStream imageStream);

    @Override
    default LoadImageCallback start() {
        return exec(new LoadImageCallback());
    }

    interface Exec extends DockerCmdAsyncExec<LoadImageAsyncCmd, LoadResponseItem> {
    }
}
