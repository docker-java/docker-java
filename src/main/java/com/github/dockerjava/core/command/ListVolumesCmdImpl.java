package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.ListVolumesCmd;
import com.github.dockerjava.api.command.ListVolumesResponse;

/**
 *
 * @author Marcus Linke
 *
 */
public class ListVolumesCmdImpl extends AbstrDockerCmd<ListVolumesCmd, ListVolumesResponse> implements ListVolumesCmd {

    private String filters;

    public ListVolumesCmdImpl(ListVolumesCmd.Exec exec) {
        super(exec);
    }

    @Override
    public String getFilters() {
        return filters;
    }

    @Override
    public ListVolumesCmd withFilters(String filter) {
        checkNotNull(filter, "filters have not been specified");
        this.filters = filter;
        return this;
    }
}
