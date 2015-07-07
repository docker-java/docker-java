package com.github.dockerjava.api.command;

import java.util.List;

import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Filters;

/**
 * List containers
 *
 * @param showAll
 *            - true or false, Show all containers. Only running containers are shown by default.
 * @param showSize
 *            - true or false, Show the containers sizes. This is false by default.
 * @param limit
 *            - Show `limit` last created containers, include non-running ones. There is no limit by default.
 * @param sinceId
 *            - Show only containers created since Id, include non-running ones.
 * @param beforeId
 *            - Show only containers created before Id, include non-running ones.
 *
 */
public interface ListContainersCmd extends DockerCmd<List<Container>> {

    public int getLimit();

    public boolean hasShowSizeEnabled();

    public boolean hasShowAllEnabled();

    public String getSinceId();

    public String getBeforeId();

    public Filters getFilters();

    public ListContainersCmd withShowAll(boolean showAll);

    public ListContainersCmd withShowSize(boolean showSize);

    public ListContainersCmd withLimit(int limit);

    public ListContainersCmd withSince(String since);

    public ListContainersCmd withBefore(String before);

    public ListContainersCmd withFilters(Filters filters);

    public static interface Exec extends DockerCmdExec<ListContainersCmd, List<Container>> {
    }

}