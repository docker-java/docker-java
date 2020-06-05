package com.github.dockerjava.junit;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerRule;

import static com.github.dockerjava.utils.TestUtils.isSwarm;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerAssume {
    public static void assumeSwarm(DockerClient client) {
        assumeTrue(isSwarm(client));
    }

    public static void assumeSwarm(String message, DockerClient client) {
        assumeTrue(message, isSwarm(client));
    }

    public static void assumeNotSwarm(DockerClient client) {
        assumeFalse(isSwarm(client));
    }

    public static void assumeNotSwarm(String message, DockerRule dockerRule) {
        assumeNotSwarm(message, dockerRule.getClient());
    }

    public static void assumeNotSwarm(String message, DockerClient client) {
        assumeFalse(message, isSwarm(client));
    }

}
