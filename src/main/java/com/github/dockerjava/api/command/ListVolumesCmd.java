package com.github.dockerjava.api.command;

import javax.annotation.CheckForNull;

/**
 * List volumes.
 *
 *
 * @author Marcus Linke
 */
public interface ListVolumesCmd extends SyncDockerCmd<ListVolumesResponse> {

    @CheckForNull
    public String getFilters();

    /**
     * @param filters
     *            - JSON encoded value of the filters (a map[string][]string) to process on the volumes list. There is
     *            one available filter: dangling=true
     */
    public ListVolumesCmd withFilters(String filters);

    public static interface Exec extends DockerCmdSyncExec<ListVolumesCmd, ListVolumesResponse> {
    }
}