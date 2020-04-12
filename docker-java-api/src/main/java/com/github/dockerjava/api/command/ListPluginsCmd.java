package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Plugin;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

public interface ListPluginsCmd extends SyncDockerCmd<List<Plugin>> {
    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param capabilities - Show only plugins with the given ids
     */
    ListPluginsCmd withCapabilityFilter(String... capabilities);

    /**
     * @param enable - Show only plugins with the given names
     */
    ListPluginsCmd withShowEnable(Boolean enable);


    interface Exec extends DockerCmdSyncExec<ListPluginsCmd, List<Plugin>> {
    }
}
