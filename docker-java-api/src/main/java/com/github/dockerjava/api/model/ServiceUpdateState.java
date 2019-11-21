package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum ServiceUpdateState {
    @FieldName("updating")
    UPDATING,

    @FieldName("paused")
    PAUSED,

    @FieldName("completed")
    COMPLETED,

    @FieldName("rollback_completed")
    ROLLBACK_COMPLETED
}
