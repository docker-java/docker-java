package com.github.dockerjava.cmd;

import com.github.dockerjava.junit.DockerRule;
import com.github.dockerjava.junit.category.Integration;
import org.junit.Rule;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static com.github.dockerjava.cmd.CmdIT.FactoryType.JERSEY;
import static com.github.dockerjava.cmd.CmdIT.FactoryType.NETTY;

/**
 * @author Kanstantsin Shautsou
 */
@Category(Integration.class)
@RunWith(Parameterized.class)
public abstract class CmdIT {
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
