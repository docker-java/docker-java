package com.github.dockerjava.junit.suite;

import com.github.dockerjava.cmd.AttachContainerCmdIT;
import com.github.dockerjava.cmd.AuthCmdIT;
import com.github.dockerjava.cmd.BuildImageCmdIT;
import com.github.dockerjava.cmd.CommitCmdIT;
import com.github.dockerjava.cmd.ConnectToNetworkCmdIT;
import com.github.dockerjava.cmd.CopyArchiveFromContainerCmdIT;
import com.github.dockerjava.cmd.CopyArchiveToContainerCmdIT;
import com.github.dockerjava.cmd.CopyFileFromContainerCmdIT;
import com.github.dockerjava.cmd.CreateContainerCmdIT;
import com.github.dockerjava.cmd.CreateNetworkCmdIT;
import com.github.dockerjava.cmd.CreateVolumeCmdIT;
import com.github.dockerjava.cmd.EventsCmdIT;
import com.github.dockerjava.cmd.InfoCmdIT;
import com.github.dockerjava.cmd.RemoveNetworkCmdIT;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Tests that requires real connection to docker.
 *
 * @author Kanstantsin Shautsou
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AttachContainerCmdIT.class,
        AuthCmdIT.class,
        BuildImageCmdIT.class,
        CommitCmdIT.class,
        CopyArchiveFromContainerCmdIT.class,
        CopyArchiveToContainerCmdIT.class,
        CopyFileFromContainerCmdIT.class,
        ConnectToNetworkCmdIT.class,
        CreateContainerCmdIT.class,
        CreateNetworkCmdIT.class,
        CreateVolumeCmdIT.class,
        EventsCmdIT.class,
        InfoCmdIT.class,
        RemoveNetworkCmdIT.class,
})
public class IntegrationDockerTestSuite {
}
