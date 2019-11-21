package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public enum PortConfigProtocol {

    @FieldName("tcp")
    TCP,

    @FieldName("udp")
    UDP

}
