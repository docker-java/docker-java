package com.github.dockerjava.client.command;

import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.client.model.Container;
import com.google.common.base.Preconditions;
import javax.ws.rs.client.WebTarget;


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
public class ListContainersCmd extends AbstrDockerCmd<ListContainersCmd, List<Container>>  {

	private static final Logger LOGGER = LoggerFactory.getLogger(ListContainersCmd.class);

	private int limit = -1;
	private boolean showSize, showAll = false;
	String sinceId, beforeId;

    public int getLimit() {
        return limit;
    }

    public boolean hasShowSizeEnabled() {
        return showSize;
    }

    public boolean hasShowAllEnabled() {
        return showAll;
    }

    public String getSinceId() {
        return sinceId;
    }

    public String getBeforeId() {
        return beforeId;
    }

    public ListContainersCmd withShowAll(boolean showAll) {
		this.showAll = showAll;
		return this;
	}

	public ListContainersCmd withShowSize(boolean showSize) {
		this.showSize = showSize;
		return this;
	}

	public ListContainersCmd withLimit(int limit) {
		Preconditions.checkArgument(limit > 0, "limit must be greater 0");
		this.limit = limit;
		return this;
	}

	public ListContainersCmd withSince(String since) {
		Preconditions.checkNotNull(since, "since was not specified");
		this.sinceId = since;
		return this;
	}

	public ListContainersCmd withBefore(String before) {
		Preconditions.checkNotNull(before, "before was not specified");
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

	protected List<Container> impl() {
		WebTarget webResource = baseResource.path("/containers/json")
                .queryParam("all", showAll ? "1" : "0")
                .queryParam("since", sinceId)
                .queryParam("before", beforeId)
                .queryParam("size", showSize ? "1" : "0");

        if (limit >= 0) {
            webResource = webResource.queryParam("limit", String.valueOf(limit));
        }

		LOGGER.trace("GET: {}", webResource);
		List<Container> containers = webResource.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<List<Container>>() {
        });
		LOGGER.trace("Response: {}", containers);

		return containers;
	}
}
