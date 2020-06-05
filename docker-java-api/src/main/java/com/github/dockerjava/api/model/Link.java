package com.github.dockerjava.api.model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Represents a network link between two Docker containers. The container with the name {@link #getName()} is made available in the target
 * container with the aliased name {@link #getAlias()}. This involves creating an entry in <code>/etc/hosts</code> and some environment
 * variables in the target container as well as creating a network bridge between both containers.
 */
@EqualsAndHashCode
public class Link implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;

    private final String alias;

    /**
     * Creates a {@link Link} for the container with the given name and an aliased name for use in the target container.
     *
     * @param name
     *            the name of the container that you want to link into the target container
     * @param alias
     *            the aliased name under which the linked container will be available in the target container
     */
    public Link(final String name, final String alias) {
        this.name = name;
        this.alias = alias;
    }

    /**
     * @return the name of the container that is linked into the target container
     */
    public String getName() {
        return name;
    }

    /**
     * @return the aliased name under which the linked container will be available in the target container
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Parses a textual link specification (as used by the Docker CLI) to a {@link Link}.
     *
     * @param serialized
     *            the specification, e.g. <code>name:alias</code> or <code>/name1:/name2/alias</code>
     * @return a {@link Link} matching the specification
     * @throws IllegalArgumentException
     *             if the specification cannot be parsed
     */
    public static Link parse(final String serialized) throws IllegalArgumentException {
        try {
            final String[] parts = serialized.split(":");
            switch (parts.length) {
                case 2: {
                    String[] nameSplit = parts[0].split("/");
                    String[] aliasSplit = parts[1].split("/");
                    return new Link(nameSplit[nameSplit.length - 1], aliasSplit[aliasSplit.length - 1]);
                }
                default: {
                    throw new IllegalArgumentException();
                }
            }
        } catch (final Exception e) {
            throw new IllegalArgumentException("Error parsing Link '" + serialized + "'");
        }
    }

    /**
     * Returns a string representation of this {@link Link} suitable for inclusion in a JSON message. The format is <code>name:alias</code>,
     * like the argument in {@link #parse(String)}.
     *
     * @return a string representation of this {@link Link}
     */
    @Override
    public String toString() {
        return name + ":" + alias;
    }

}
