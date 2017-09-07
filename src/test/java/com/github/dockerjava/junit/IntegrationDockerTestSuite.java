package com.github.dockerjava.junit;

import com.github.dockerjava.cmd.AttachContainerCmdTest;
import com.github.dockerjava.cmd.AuthCmdTest;
import com.github.dockerjava.cmd.BuildImageCmdTest;
import com.github.dockerjava.cmd.InfoCmdTest;
import com.github.dockerjava.cmd.RemoveNetworkCmdTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests that requires real connection to docker.
 *
 * @author Kanstantsin Shautsou
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AttachContainerCmdTest.class,
        AuthCmdTest.class,
        BuildImageCmdTest.class,
        InfoCmdTest.class,
        RemoveNetworkCmdTest.class,
})
public class IntegrationDockerTestSuite {
}
