package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Links implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Link[] links;

    public Links(final Link... links) {
        this.links = links;
    }

    public Links(final List<Link> links) {
        this.links = links.toArray(new Link[links.size()]);
    }

    public Link[] getLinks() {
        return links;
    }

    @JsonCreator
    public static Links fromPrimitive(String[] links) {
        return new Links(
                Stream.of(links).map(Link::parse).toArray(Link[]::new)
        );
    }

    @JsonValue
    public String[] toPrimitive() {
        return Stream.of(links).map(Link::toString).toArray(String[]::new);
    }
}
