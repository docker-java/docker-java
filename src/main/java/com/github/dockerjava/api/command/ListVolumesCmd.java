package com.github.dockerjava.api.command;

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
    public Map<String, List<String>> getFilters();

    /**
     * @param dangling
     *            - Show dangling volumes filter
     */
    public ListVolumesCmd withDanglingFilter(Boolean dangling);

    public static interface Exec extends DockerCmdSyncExec<ListVolumesCmd, ListVolumesResponse> {
    }
}
