package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.util.DockerImageName;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import java.io.IOException;

import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

@Test(groups = "integration")
public class InspectExecCmdImplTest extends AbstractDockerClientTest {
    @BeforeTest
    public void beforeTest() throws DockerException {
        super.beforeTest();
    }

    @AfterTest
    public void afterTest() {
        super.afterTest();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        super.afterMethod(result);
    }

    @Test(groups = "ignoreInCircleCi")
    public void inspectExecTest() throws IOException {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient
                .createContainerCmd(new DockerImageName("busybox"))
                        .withCmd("top")
                        .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse touchFileCmdCreateResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout()
                .withAttachStderr()
                .withCmd("touch", "/marker").exec();
        LOG.info("Created exec {}", touchFileCmdCreateResponse.toString());
        assertThat(touchFileCmdCreateResponse.getId(), not(isEmptyString()));
        ExecCreateCmdResponse checkFileCmdCreateResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout()
                .withAttachStderr()
                .withCmd("test", "-e", "/marker").exec();
        LOG.info("Created exec {}", checkFileCmdCreateResponse.toString());
        assertThat(checkFileCmdCreateResponse.getId(), not(isEmptyString()));

        // Check that file does not exist
        InputStream response1 = dockerClient.execStartCmd(container.getId())
                .withExecId(checkFileCmdCreateResponse.getId())
                .exec();
        asString(response1);  // consume

        InspectExecResponse first = dockerClient.inspectExecCmd(checkFileCmdCreateResponse.getId()).exec();
        assertThat(first.getExitCode(), is(1));

        // Create the file
        InputStream response2 = dockerClient.execStartCmd(container.getId())
                .withExecId(touchFileCmdCreateResponse.getId())
                .exec();
        asString(response2);

        InspectExecResponse second = dockerClient.inspectExecCmd(touchFileCmdCreateResponse.getId()).exec();
        assertThat(second.getExitCode(), is(0));

        // Check that file does exist now
        InputStream response3 = dockerClient.execStartCmd(container.getId())
                .withExecId(checkFileCmdCreateResponse.getId())
                .exec();
        asString(response3);

        InspectExecResponse third = dockerClient.inspectExecCmd(checkFileCmdCreateResponse.getId()).exec();
        assertThat(third.getExitCode(), is(0));

        // Get container info and check its roundtrip to ensure the consistency
        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());
        JSONTestHelper.testRoundTrip(containerInfo);
    }
}
