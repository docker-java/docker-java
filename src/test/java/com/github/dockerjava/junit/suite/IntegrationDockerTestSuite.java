package com.github.dockerjava.junit.suite;

import com.github.dockerjava.cmd.AttachContainerCmdTest;
import com.github.dockerjava.cmd.AuthCmdTest;
import com.github.dockerjava.cmd.BuildImageCmdTest;
import com.github.dockerjava.cmd.CommitCmdTest;
import com.github.dockerjava.cmd.ConnectToNetworkCmdTest;
import com.github.dockerjava.cmd.CopyArchiveFromContainerCmdTest;
import com.github.dockerjava.cmd.CopyArchiveToContainerCmdTest;
import com.github.dockerjava.cmd.CopyFileFromContainerCmdTest;
import com.github.dockerjava.cmd.CreateContainerCmdTest;
import com.github.dockerjava.cmd.CreateNetworkCmdTest;
import com.github.dockerjava.cmd.CreateVolumeCmdTest;
import com.github.dockerjava.cmd.EventsCmdTest;
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
        CommitCmdTest.class,
        CopyArchiveFromContainerCmdTest.class,
        CopyArchiveToContainerCmdTest.class,
        CopyFileFromContainerCmdTest.class,
        ConnectToNetworkCmdTest.class,
        CreateContainerCmdTest.class,
        CreateNetworkCmdTest.class,
        CreateVolumeCmdTest.class,
        EventsCmdTest.class,
        InfoCmdTest.class,
        RemoveNetworkCmdTest.class,
})
public class IntegrationDockerTestSuite {
}
