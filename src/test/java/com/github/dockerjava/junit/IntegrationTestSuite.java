package com.github.dockerjava.junit;

import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.cmd.InfoCmdTest;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Arrays;

/**
 * Tests that requires real connection to docker.
 *
 * @author Kanstantsin Shautsou
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        InfoCmdTest.class
})
public class IntegrationTestSuite {

    public static Iterable<DockerCmdExecFactory> factories() {
        return Arrays.asList(
                new JerseyDockerCmdExecFactory(),
                new NettyDockerCmdExecFactory()
        );
    }
}
