package com.github.dockerjava.transport;

import org.osgi.service.component.annotations.ComponentPropertyType;

@ComponentPropertyType
public @interface DockerHttpClientType {

    public static final String TRANSPORT_UNIX_SOCKETS = "unix.sockets";

    public static final String TRANSPORT_WINDOWS_NPIPE = "windows.npipe";

    public static final String TRANSPORT_STDIN_ATTACHMENT = "stdin.attacgment";

    String value();

    String[] transports();
}
