package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.util.FiltersBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * List containers.
 */
public class ListContainersCmdImpl extends AbstrDockerCmd<ListContainersCmd, List<Container>> implements
        ListContainersCmd {

    private Integer limit = -1;

    private Boolean showSize, showAll = false;

    private String sinceId, beforeId;

    private FiltersBuilder filters = new FiltersBuilder();

    public ListContainersCmdImpl(ListContainersCmd.Exec exec) {
        super(exec);
    }

    @Override
    public Integer getLimit() {
        return limit;
    }

    @Override
    public Boolean hasShowSizeEnabled() {
        return showSize;
    }

    @Override
    public Boolean hasShowAllEnabled() {
        return showAll;
    }

    @Override
    public String getSinceId() {
        return sinceId;
    }

    @Override
    public String getBeforeId() {
        return beforeId;
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListContainersCmd withShowAll(Boolean showAll) {
        this.showAll = showAll;
        return this;
    }

    @Override
    public ListContainersCmd withShowSize(Boolean showSize) {
        this.showSize = showSize;
        return this;
    }

    @Override
    public ListContainersCmd withLimit(Integer limit) {
        Objects.requireNonNull(limit, "limit was not specified");
        checkArgument(limit > 0, "limit must be greater 0");
        this.limit = limit;
        return this;
    }

    @Override
    public ListContainersCmd withSince(String since) {
        this.sinceId = Objects.requireNonNull(since, "since was not specified");
        return this;
    }

    @Override
    public ListContainersCmd withBefore(String before) {
        this.beforeId = Objects.requireNonNull(before, "before was not specified");
        return this;
    }

    @Override
    public ListContainersCmd withNameFilter(Collection<String> name) {
        return withFilter("name", name);
    }

    @Override
    public ListContainersCmd withIdFilter(Collection<String> id) {
        return withFilter("id", id);
    }

    @Override
    public ListContainersCmd withAncestorFilter(Collection<String> ancestor) {
        return withFilter("ancestor", ancestor);
    }

    @Override
    public ListContainersCmd withVolumeFilter(Collection<String> volume) {
        return withFilter("volume", volume);
    }

    @Override
    public ListContainersCmd withNetworkFilter(Collection<String> network) {
        return withFilter("network", network);
    }

    @Override
    public ListContainersCmd withLabelFilter(Collection<String> labels) {
        return withFilter("label", labels);
    }

    @Override
    public ListContainersCmd withLabelFilter(Map<String, String> labels) {
        Objects.requireNonNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }

    @Override
    public ListContainersCmd withExitedFilter(Integer exited) {
        Objects.requireNonNull(exited, "exited was not specified");
        this.filters.withFilter("exited", exited.toString());
        return this;
    }

    @Override
    public ListContainersCmd withFilter(String filterName, Collection<String> filterValues) {
        Objects.requireNonNull(filterValues, filterName + " was not specified");
        this.filters.withFilter(filterName, filterValues);
        return this;
    }

    @Override
    public ListContainersCmd withStatusFilter(Collection<String> status) {
        Objects.requireNonNull(status, "status was not specified");
        this.filters.withFilter("status", status);
        return this;
    }
}
