package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Container;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

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
     * @param name
     *            - Show only containers that has the container's name
     */
    ListContainersCmd withNameFilter(String... name);

    /**
     * @param id
     *            - Show only containers that has the container's id
     */
    ListContainersCmd withIdFilter(String... id);

    /**
     * @param ancestor
     *            - Show only containers created from an image or a descendant.
     */
    ListContainersCmd withAncestorFilter(String... ancestor);

    /**
     * @param volume
     *            - Show only containers with volume name or mount point destination
     */
    ListContainersCmd withVolumeFilter(String... volume);

    /**
     * @param network
     *            - Show only containers with network id or network name
     */
    ListContainersCmd withNetworkFilter(String... network);

    /**
     * @param exited
     *            - Show only containers that exited with the passed exitcode.
     */
    ListContainersCmd withExitedFilter(Integer exited);

    /**
     * Use {@link #withExitedFilter(Integer)} instead
     * @param exitcode
     *            - Show only containers that exited with the passed exitcode.
     */
    @Deprecated
    ListContainersCmd withExitcodeFilter(Integer exitcode);

    /**
     * @param status
     *            - Show only containers with the passed status (created|restarting|running|paused|exited).
     */
    ListContainersCmd withStatusFilter(String... status);

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

    /**
     * @param filterName
     * @param filterValues
     *            - Show only containers where the filter matches the given values
     */
    ListContainersCmd withFilter(String filterName, String... filterValues);

    interface Exec extends DockerCmdSyncExec<ListContainersCmd, List<Container>> {
    }

}
