package com.github.dockerjava.core.command;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.core.util.FiltersBuilder;

/**
 *
 * @author Marcus Linke
 *
 */
public class ListVolumesCmdImpl extends AbstrDockerCmd<ListVolumesCmd, ListVolumesResponse> implements ListVolumesCmd {

    private FiltersBuilder filters = new FiltersBuilder();

    public ListVolumesCmdImpl(ListVolumesCmd.Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListVolumesCmd withDanglingFilter(Boolean dangling) {
        Objects.requireNonNull(dangling, "dangling have not been specified");
        this.filters.withFilter("dangling", dangling.toString());
        return this;
    }

    @Override
    public ListVolumesCmd withFilter(String filterName, Collection<String> filterValues) {
        Objects.requireNonNull(filterValues, filterName + " was not specified");
        this.filters.withFilter(filterName, filterValues);
        return this;
    }
}
