package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.util.FiltersBuilder;

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
        checkNotNull(limit, "limit was not specified");
        checkArgument(limit > 0, "limit must be greater 0");
        this.limit = limit;
        return this;
    }

    @Override
    public ListContainersCmd withSince(String since) {
        checkNotNull(since, "since was not specified");
        this.sinceId = since;
        return this;
    }

    @Override
    public ListContainersCmd withBefore(String before) {
        checkNotNull(before, "before was not specified");
        this.beforeId = before;
        return this;
    }

    @Override
    public ListContainersCmd withLabelFilter(String... labels) {
        checkNotNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }

    @Override
    public ListContainersCmd withLabelFilter(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }

    @Override
    public ListContainersCmd withExitcodeFilter(Integer exitcode) {
        checkNotNull(exitcode, "exitcode was not specified");
        this.filters.withFilter("exitcode", exitcode.toString());
        return this;
    }

    @Override
    public ListContainersCmd withStatusFilter(String status) {
        checkNotNull(status, "status was not specified");
        this.filters.withFilter("status", status);
        return this;
    }
}
