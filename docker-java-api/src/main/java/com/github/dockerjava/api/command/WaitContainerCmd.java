package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.WaitResponse;

/**
 * Wait a container
 *
 * Block until container stops, then returns its exit code
 */
public interface WaitContainerCmd extends AsyncDockerCmd<WaitContainerCmd, WaitResponse> {

    @CheckForNull
    String getContainerId();

    WaitContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * @throws NotFoundException
     *             container not found
     */
    @Override
    <T extends ResultCallback<WaitResponse>> T exec(T resultCallback);

    @Override
    default WaitContainerResultCallback start() {
        return exec(new WaitContainerResultCallback());
    }

    interface Exec extends DockerCmdAsyncExec<WaitContainerCmd, WaitResponse> {
    }

}
