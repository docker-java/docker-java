package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListConfigsCmd;
import com.github.dockerjava.api.model.Config;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * List configs.
 */
public class ListConfigsCmdImpl extends AbstrDockerCmd<ListConfigsCmd, List<Config>> implements ListConfigsCmd {

    private Map<String, List<String>> filters = Collections.emptyMap();

    public ListConfigsCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters;
    }

    public ListConfigsCmd withFilters(Map<String, List<String>> filters) {
        this.filters = Objects.requireNonNull(filters, "filters was not specified");
        return this;
    }
}
