package com.github.dockerjava.core.command;

import static jersey.repackaged.com.google.common.base.Preconditions.checkArgument;
import static jersey.repackaged.com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.model.Container;


/**
 * List containers
 *
 * @param showAll - true or false, Show all containers. Only running containers are shown by default.
 * @param showSize - true or false, Show the containers sizes. This is false by default.
 * @param limit - Show `limit` last created containers, include non-running ones. There is no limit by default.
 * @param sinceId - Show only containers created since Id, include non-running ones.
 * @param beforeId - Show only containers created before Id, include non-running ones.
 *
 */
public class ListContainersCmdImpl extends AbstrDockerCmd<ListContainersCmd, List<Container>> implements ListContainersCmd  {

	private int limit = -1;
	
	private boolean showSize, showAll = false;
	
	private String sinceId, beforeId;
	
	public ListContainersCmdImpl(ListContainersCmd.Exec exec) {
		super(exec);
	}

    @Override
	public int getLimit() {
        return limit;
    }

    @Override
	public boolean hasShowSizeEnabled() {
        return showSize;
    }

    @Override
	public boolean hasShowAllEnabled() {
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
	public ListContainersCmd withShowAll(boolean showAll) {
		this.showAll = showAll;
		return this;
	}

	@Override
	public ListContainersCmd withShowSize(boolean showSize) {
		this.showSize = showSize;
		return this;
	}

	@Override
	public ListContainersCmd withLimit(int limit) {
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
    public String toString() {
        return new StringBuilder("ps ")
            .append(showAll ? "--all=true" : "")
            .append(showSize ? "--size=true" : "")
            .append(sinceId != null ? "--since " + sinceId : "")
            .append(beforeId != null ? "--before " + beforeId : "")
            .append(limit != -1 ? "-n " + limit : "")
            .toString();
    }
}
