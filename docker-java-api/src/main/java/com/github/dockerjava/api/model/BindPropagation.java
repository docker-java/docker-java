package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum BindPropagation {
    @FieldName("rprivate")
    R_PRIVATE,

    @FieldName("private")
    PRIVATE,

    @FieldName("rshared")
    R_SHARED,

    @FieldName("shared")
    SHARED,

    @FieldName("rslave")
    R_SLAVE,

    @FieldName("slave")
    SLAVE
}
