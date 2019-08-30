package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.RemoteApiVersion;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * Command to list all services in a docker swarm. Only applicable if docker runs in swarm mode.
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public interface ListServicesCmd extends SyncDockerCmd<List<Service>> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param ids - Show only services with the given ids
     */
    ListServicesCmd withIdFilter(List<String> ids);

    /**
     * @param names - Show only services with the given names
     */
    ListServicesCmd withNameFilter(List<String> names);

    /**
     * @param labels - Show only services with the passed labels. Labels is a {@link Map} that contains label keys and values
     */
    ListServicesCmd withLabelFilter(Map<String, String> labels);

    interface Exec extends DockerCmdSyncExec<ListServicesCmd, List<Service>> {
    }

}
