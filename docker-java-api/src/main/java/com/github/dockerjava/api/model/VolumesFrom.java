package com.github.dockerjava.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class VolumesFrom implements Serializable {
    private static final long serialVersionUID = 1L;

    private String container;

    private AccessMode accessMode;

    public VolumesFrom(String container) {
        this(container, AccessMode.DEFAULT);
    }

    public VolumesFrom(String container, AccessMode accessMode) {
        this.container = container;
        this.accessMode = accessMode;
    }

    public String getContainer() {
        return container;
    }

    public AccessMode getAccessMode() {
        return accessMode;
    }

    /**
     * Parses a volume from specification to a {@link VolumesFrom}.
     *
     * @param serialized
     *            the specification, e.g. <code>container:ro</code>
     * @return a {@link VolumesFrom} matching the specification
     * @throws IllegalArgumentException
     *             if the specification cannot be parsed
     */
    @JsonCreator
    public static VolumesFrom parse(String serialized) {
        try {
            String[] parts = serialized.split(":");
            switch (parts.length) {
                case 1: {
                    return new VolumesFrom(parts[0]);
                }
                case 2: {
                    return new VolumesFrom(parts[0], AccessMode.valueOf(parts[1]));
                }

                default: {
                    throw new IllegalArgumentException();
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing Bind '" + serialized + "'");
        }
    }

    /**
     * Returns a string representation of this {@link VolumesFrom} suitable for inclusion in a JSON message. The format is
     * <code>&lt;container&gt;:&lt;access mode&gt;</code>, like the argument in {@link #parse(String)}.
     *
     * @return a string representation of this {@link VolumesFrom}
     */
    @Override
    @JsonValue
    public String toString() {
        return container + ":" + accessMode.toString();
    }

}
