package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.junit.DockerRule;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

/**
 * @author Kanstantsin Shautsou
 */
@RunWith(Parameterized.class)
public abstract class CmdTest {
    @Parameterized.Parameters(name = "{index}:{0}")
    public static Iterable<DockerCmdExecFactory> data() {
        return Arrays.asList(
                new JerseyDockerCmdExecFactory(),
                new NettyDockerCmdExecFactory()
        );
    }

    @Parameterized.Parameter
    public static DockerCmdExecFactory cmdExecFactory;

    @ClassRule
    public static DockerRule dockerRule = new DockerRule(cmdExecFactory);

}
