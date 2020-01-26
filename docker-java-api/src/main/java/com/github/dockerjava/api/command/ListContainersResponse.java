package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Container;

import java.util.List;

public final class ListContainersResponse {

    private final List<Container> containers;

    public ListContainersResponse(List<Container> containers) {
        this.containers = containers;
    }

    public List<Container> getContainers() {
        return containers;
    }
}
