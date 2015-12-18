package com.github.dockerjava.api.command;

import java.util.List;

import javax.annotation.CheckForNull;

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
public interface ListContainersCmd extends SyncDockerCmd<List<Container>> {

    @CheckForNull
    public String getBeforeId();

    @CheckForNull
    public Filters getFilters();

    @CheckForNull
    public Integer getLimit();

    @CheckForNull
    public String getSinceId();

    @CheckForNull
    public Boolean hasShowAllEnabled();

    @CheckForNull
    public Boolean hasShowSizeEnabled();

    public ListContainersCmd withBefore(String before);

    public ListContainersCmd withFilters(Filters filters);

    public ListContainersCmd withLimit(Integer limit);

    public ListContainersCmd withShowAll(Boolean showAll);

    public ListContainersCmd withShowSize(Boolean showSize);

    public ListContainersCmd withSince(String since);

    public static interface Exec extends DockerCmdSyncExec<ListContainersCmd, List<Container>> {
    }

}
