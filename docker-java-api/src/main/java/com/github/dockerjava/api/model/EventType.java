package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 1.24
 */
public enum EventType {
    /**
     * @since 1.24
     */
    CONTAINER("container"),

    /**
     * @since 1.24
     */
    DAEMON("daemon"),

    /**
     * @since 1.24
     */
    IMAGE("image"),
    NETWORK("network"),
    PLUGIN("plugin"),
    VOLUME("volume");

    private static final Map<String, EventType> EVENT_TYPES = new HashMap<>();

    static {
        for (EventType t : values()) {
            EVENT_TYPES.put(t.name().toLowerCase(), t);
        }
    }

    private String value;

    EventType(@Nonnull String value) {
        this.value = value;
    }

    @ToPrimitive
    public String getValue() {
        return value;
    }

    @FromPrimitive
    public static EventType forValue(String s) {
        return EVENT_TYPES.get(s);
    }
}
