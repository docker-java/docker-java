package com.github.dockerjava.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.RemoteApiVersion;

/**
 * @author Kanstantsin Shautsou
 */
public class TestUtils {
    private TestUtils() {
    }

    public static RemoteApiVersion getVersion(DockerClient client) {
        final String serverVersion = client.versionCmd().exec().getApiVersion();
        return RemoteApiVersion.parseConfig(serverVersion);
    }

    public static boolean isSwarm(DockerClient client) {
        final String serverVersion = client.versionCmd().exec().getVersion();
        return serverVersion.startsWith("swarm/");
    }

    public static boolean isNotSwarm(DockerClient client) {
        return !isSwarm(client);
    }
}
