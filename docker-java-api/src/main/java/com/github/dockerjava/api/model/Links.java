package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Stream;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

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

    @FromPrimitive
    public static Links fromPrimitive(String[] links) {
        return new Links(
                Stream.of(links).map(Link::parse).toArray(Link[]::new)
        );
    }

    @ToPrimitive
    public String[] toPrimitive() {
        return Stream.of(links).map(Link::toString).toArray(String[]::new);
    }
}
