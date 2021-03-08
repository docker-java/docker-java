package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Task;

import javax.annotation.CheckForNull;

public interface InspectTaskCmd extends SyncDockerCmd<Task> {
    @CheckForNull
    String getTaskId();

    InspectTaskCmd withTaskId(String taskId);

    @Override
    Task exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<InspectTaskCmd, Task> {
    }
}
