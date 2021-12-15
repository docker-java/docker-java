package com.github.dockerjava.api.command;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.CheckForNull;

/**
 * List volumes.
 *
 * @author Marcus Linke
 */
public interface ListVolumesCmd extends SyncDockerCmd<ListVolumesResponse> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param dangling
     *            - Show dangling volumes filter
     */
    ListVolumesCmd withDanglingFilter(Boolean dangling);

    /**
     * @param filterName
     * @param filterValues
     *            - Show only volumes where the filter matches the given values
     */
    ListVolumesCmd withFilter(String filterName, Collection<String> filterValues);

    interface Exec extends DockerCmdSyncExec<ListVolumesCmd, ListVolumesResponse> {
    }
}
