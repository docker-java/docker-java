package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Task;
import com.github.dockerjava.api.model.TaskState;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

public interface ListTasksCmd extends SyncDockerCmd<List<Task>> {
    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param labels - Show only tasks with the passed labels.
     *               Labels is a {@link Map} that contains label keys and values
     */
    ListTasksCmd withLabelFilter(Map<String, String> labels);

    /**
     * @param labels - Show only tasks with the passed labels.
     */
    ListTasksCmd withLabelFilter(String... labels);

    /**
     * @param ids Task id(s)
     */
    ListTasksCmd withIdFilter(String... ids);

    /**
     * @param names Task name(s)
     */
    ListTasksCmd withNameFilter(String... names);

    /**
     * @param nodeNames Node id(s) or name(s)
     */
    ListTasksCmd withNodeFilter(String... nodeNames);

    /**
     * @param serviceNames Service name(s)
     */
    ListTasksCmd withServiceFilter(String... serviceNames);

    /**
     * @param desiredState The desired-state filter can take the values running, shutdown, or accepted.
     */
    ListTasksCmd withStateFilter(TaskState... desiredState);

    @Override
    List<Task> exec() throws NotFoundException;

    interface Exec extends DockerCmdSyncExec<ListTasksCmd, List<Task>> {
    }
}
