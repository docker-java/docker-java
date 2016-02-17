package com.github.dockerjava.api.command;

import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;

import com.github.dockerjava.api.model.Container;

/**
 * List containers
 *
 */
public interface ListContainersCmd extends SyncDockerCmd<List<Container>> {

    @CheckForNull
    public String getBeforeId();

    @CheckForNull
    public Map<String, List<String>> getFilters();

    @CheckForNull
    public Integer getLimit();

    @CheckForNull
    public String getSinceId();

    @CheckForNull
    public Boolean hasShowAllEnabled();

    @CheckForNull
    public Boolean hasShowSizeEnabled();

    /**
     * @param before
     *            - Show only containers created before Id, include non-running ones.
     */
    public ListContainersCmd withBefore(String before);

    /**
     * @param exitcode
     *            - Show only containers that exited with the passed exitcode.
     */
    public ListContainersCmd withExitcodeFilter(Integer exitcode);

    /**
     * @param status
     *            - Show only containers with the passed status (created|restarting|running|paused|exited).
     */
    public ListContainersCmd withStatusFilter(String status);

    /**
     * @param labels
     *            - Show only containers with the passed labels.
     */
    public ListContainersCmd withLabelFilter(String... labels);

    /**
     * @param labels
     *            - Show only containers with the passed labels. Labels is a {@link Map} that contains label keys and values
     */
    public ListContainersCmd withLabelFilter(Map<String, String> labels);

    /**
     * @param limit
     *            - Show `limit` last created containers, include non-running ones. There is no limit by default.
     */
    public ListContainersCmd withLimit(Integer limit);

    /**
     * @param showAll
     *            - Show all containers. Only running containers are shown by default.
     */
    public ListContainersCmd withShowAll(Boolean showAll);

    /**
     * @param showSize
     *            - Show the containers sizes. This is false by default.
     */
    public ListContainersCmd withShowSize(Boolean showSize);

    /**
     * @param since
     *            - Show only containers created since Id, include non-running ones.
     */
    public ListContainersCmd withSince(String since);

    public static interface Exec extends DockerCmdSyncExec<ListContainersCmd, List<Container>> {
    }

}
