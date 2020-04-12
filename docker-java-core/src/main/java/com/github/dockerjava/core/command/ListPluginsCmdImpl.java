package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListPluginsCmd;
import com.github.dockerjava.api.model.Plugin;
import com.github.dockerjava.core.util.FiltersBuilder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * List services.
 */
public class ListPluginsCmdImpl extends AbstrDockerCmd<ListPluginsCmd, List<Plugin>> implements
        ListPluginsCmd {

    private FiltersBuilder filters = new FiltersBuilder();

    public ListPluginsCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListPluginsCmd withCapabilityFilter(String... capabilities) {
        filters.withFilter("capability", capabilities);
        return this;
    }

    @Override
    public ListPluginsCmd withShowEnable(Boolean enable) {
        checkNotNull(enable, "enable have not been specified");
        filters.withFilter("enable", enable.toString());
        return this;
    }


}
