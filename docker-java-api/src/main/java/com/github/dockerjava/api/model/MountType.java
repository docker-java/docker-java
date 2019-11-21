package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum MountType {
    @FieldName("bind")
    BIND,

    @FieldName("volume")
    VOLUME,

    //@since 1.29
    @FieldName("tmpfs")
    TMPFS

}
