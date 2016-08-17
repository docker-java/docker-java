package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Service;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * List services
 *
 */
public interface ListServicesCmd extends SyncDockerCmd<List<Service>> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param ids
     *            - Show only services with the given ids
     */
    ListServicesCmd withIdFilter(List<String> ids);
    /**
     *
     * @param names
     *            - Show only services with the given names
     */
    ListServicesCmd withNameFilter(List<String> names);

    interface Exec extends DockerCmdSyncExec<ListServicesCmd, List<Service>> {
    }

}
