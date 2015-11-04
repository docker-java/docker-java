package com.github.dockerjava.api.command;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.api.model.WaitResponse;

/**
 * Wait a container
 *
 * Block until container stops, then returns its exit code
 */
public interface WaitContainerCmd extends AsyncDockerCmd<WaitContainerCmd, WaitResponse> {

    public String getContainerId();

    public WaitContainerCmd withContainerId(String containerId);

    /**
     * @throws NotFoundException
     *             container not found
     */
    @Override
    public <T extends ResultCallback<WaitResponse>> T exec(T resultCallback);

    public static interface Exec extends DockerCmdAsyncExec<WaitContainerCmd, WaitResponse> {
    }

}