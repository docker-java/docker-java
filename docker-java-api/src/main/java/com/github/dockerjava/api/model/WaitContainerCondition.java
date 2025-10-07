package com.github.dockerjava.api.model;

import javax.annotation.Nonnull;

/**
 * Docker Engine API <em>wait</em> conditions (added in v1.30).
 *
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
public enum WaitContainerCondition {
    NOT_RUNNING("not-running"),
    NEXT_EXIT("next-exit"),
    REMOVED("removed");

    @Nonnull
    private final String value;

    WaitContainerCondition(@Nonnull String value) {
        this.value = value;
    }

    @Nonnull
    public String getValue() {
        return value;
    }
}
