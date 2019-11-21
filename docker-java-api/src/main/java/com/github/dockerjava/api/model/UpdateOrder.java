package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_36}
 */
public enum UpdateOrder {
    @FieldName("stop-first")
    STOP_FIRST,

    @FieldName("start-first")
    START_FIRST
}
