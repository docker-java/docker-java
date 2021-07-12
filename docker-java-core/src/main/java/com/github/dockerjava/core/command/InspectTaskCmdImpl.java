package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.InspectTaskCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Task;

import javax.annotation.CheckForNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class InspectTaskCmdImpl extends AbstrDockerCmd<InspectTaskCmd, Task> implements InspectTaskCmd {

    private String taskId;


    public InspectTaskCmdImpl(Exec exec, String taskId) {
        super(exec);
        this.taskId = taskId;
    }

    @CheckForNull
    @Override
    public String getTaskId() {
        return this.taskId;
    }

    @Override
    public InspectTaskCmd withTaskId(String taskId) {
        checkNotNull(taskId, "taskId was not specified");
        this.taskId = taskId;
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such task
     */
    @Override
    public Task exec() throws NotFoundException {
        return super.exec();
    }
}
