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
    String getBeforeId();

    @CheckForNull
    Map<String, List<String>> getFilters();

    @CheckForNull
    Integer getLimit();

    @CheckForNull
    String getSinceId();

    @CheckForNull
    Boolean hasShowAllEnabled();

    @CheckForNull
    Boolean hasShowSizeEnabled();

    /**
     * @param before
     *            - Show only containers created before Id, include non-running ones.
     */
    ListContainersCmd withBefore(String before);

    /**
     * @param exitcode
     *            - Show only containers that exited with the passed exitcode.
     */
    ListContainersCmd withExitcodeFilter(Integer exitcode);

    /**
     * @param status
     *            - Show only containers with the passed status (created|restarting|running|paused|exited).
     */
    ListContainersCmd withStatusFilter(String status);

    /**
     * @param labels
     *            - Show only containers with the passed labels.
     */
    ListContainersCmd withLabelFilter(String... labels);

    /**
     * @param labels
     *            - Show only containers with the passed labels. Labels is a {@link Map} that contains label keys and values
     */
    ListContainersCmd withLabelFilter(Map<String, String> labels);

    /**
     * @param limit
     *            - Show `limit` last created containers, include non-running ones. There is no limit by default.
     */
    ListContainersCmd withLimit(Integer limit);

    /**
     * @param showAll
     *            - Show all containers. Only running containers are shown by default.
     */
    ListContainersCmd withShowAll(Boolean showAll);

    /**
     * @param showSize
     *            - Show the containers sizes. This is false by default.
     */
    ListContainersCmd withShowSize(Boolean showSize);

    /**
     * @param since
     *            - Show only containers created since Id, include non-running ones.
     */
    ListContainersCmd withSince(String since);

    interface Exec extends DockerCmdSyncExec<ListContainersCmd, List<Container>> {
    }

}
