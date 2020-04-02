package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Config;
import java.util.List;
import java.util.Map;
import javax.annotation.CheckForNull;

/**
 * List Configs
 *
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
public interface ListConfigsCmd extends SyncDockerCmd<List<Config>> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param ids - Show only configs with the given ids
     */
    ListConfigsCmd withIdFilter(List<String> ids);

    /**
     * @param names - Show only configs with the given names
     */
    ListConfigsCmd withNameFilter(List<String> names);

    /**
     * @param labels - Show only configs with the passed labels. Labels is a
     * {@link Map} that contains label keys and values
     */
    ListConfigsCmd withLabelFilter(Map<String, String> labels);

    interface Exec extends DockerCmdSyncExec<ListConfigsCmd, List<Config>> {
    }
}
