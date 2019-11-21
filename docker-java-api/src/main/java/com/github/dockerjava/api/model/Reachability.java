package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum Reachability {

    @FieldName("unknown")
    UNKNOWN,

    @FieldName("unreachable")
    UNREACHABLE,

    @FieldName("reachable")
    REACHABLE
}
