package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.core.util.Filters;

/**
 *
 * @author Marcus Linke
 *
 */
public class ListVolumesCmdImpl extends AbstrDockerCmd<ListVolumesCmd, ListVolumesResponse> implements ListVolumesCmd {

    private Filters filters = new Filters();

    public ListVolumesCmdImpl(ListVolumesCmd.Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.getFilters();
    }

    @Override
    public ListVolumesCmd withDanglingFilter(Boolean dangling) {
        checkNotNull(dangling, "dangling have not been specified");
        this.filters.withFilter("dangling", dangling.toString());
        return this;
    }
}
