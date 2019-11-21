package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * https://github.com/moby/moby/blob/master/api/types/swarm/swarm.go#L174-L188
 *
 * @since 1.24
 */
public enum LocalNodeState {

    INACTIVE("inactive"),
    PENDING("pending"),
    ACTIVE("active"),
    ERROR("error"),
    LOCKED("locked"),

    /**
     * Can not construct instance of com.github.dockerjava.api.model.LocalNodeState
     * from String value '': value not one of declared Enum instance names: [error, locked, inactive, active, pending]
     */
    EMPTY("");

    private static final Map<String, LocalNodeState> TYPES = new HashMap<>();

    static {
        for (LocalNodeState t : values()) {
            TYPES.put(t.name().toLowerCase(), t);
        }
    }

    private String value;

    LocalNodeState(@Nonnull String value) {
        this.value = value;
    }

    @ToPrimitive
    public String getValue() {
        return value;
    }

    @FromPrimitive
    public static LocalNodeState forValue(String s) {
        return TYPES.get(s);
    }

}
