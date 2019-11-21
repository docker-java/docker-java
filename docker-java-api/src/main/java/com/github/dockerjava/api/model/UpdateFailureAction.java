package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum UpdateFailureAction {

    @FieldName("pause")
    PAUSE,

    @FieldName("continue")
    CONTINUE,

    @FieldName("rollback")
    ROLLBACK

}
