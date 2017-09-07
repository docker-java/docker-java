package com.github.dockerjava.junit;

import com.github.dockerjava.api.DockerClient;

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
        assumeFalse(message, isSwarm(dockerRule.getClient()));
    }

}
