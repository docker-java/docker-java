package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.junit.DockerRule;
import com.github.dockerjava.junit.IntegrationTestSuite;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author Kanstantsin Shautsou
 */
@RunWith(Parameterized.class)
public abstract class CmdTest {
    @Parameterized.Parameters(name = "{index}:{0}" )
    public static Iterable<DockerCmdExecFactory> data() {
        return IntegrationTestSuite.factories();
    }

    @Parameterized.Parameter
    public static DockerCmdExecFactory cmdExecFactory;

    @Rule
    public DockerRule dockerRule = new DockerRule(cmdExecFactory);

}
