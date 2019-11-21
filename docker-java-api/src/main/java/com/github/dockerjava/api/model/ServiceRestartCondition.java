package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum ServiceRestartCondition {

    @FieldName("on-failure")
    ON_FAILURE,

    @FieldName("any")
    ANY,

    @FieldName("none")
    NONE

}
