package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import com.github.dockerjava.junit.DockerRule;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static com.github.dockerjava.cmd.CmdTest.FactoryType.JERSEY;
import static com.github.dockerjava.cmd.CmdTest.FactoryType.NETTY;

/**
 * @author Kanstantsin Shautsou
 */
@RunWith(Parameterized.class)
public abstract class CmdTest {
    public enum FactoryType {
        NETTY, JERSEY
    }

    @Parameterized.Parameters(name = "{index}:{0}")
    public static Iterable<FactoryType> data() {
        return Arrays.asList(
                NETTY, JERSEY
        );
    }

    @Parameterized.Parameter
    public FactoryType factoryType;

    public FactoryType getFactoryType() {
        return factoryType;
    }

    @Rule
    public DockerRule dockerRule = new DockerRule( this);

}
