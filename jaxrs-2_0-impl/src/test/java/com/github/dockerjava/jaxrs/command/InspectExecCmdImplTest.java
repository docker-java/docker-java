package com.github.dockerjava.jaxrs.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;
import com.github.dockerjava.test.serdes.JSONTestHelper;

@Test(groups = "integration")
public class InspectExecCmdImplTest extends AbstractDockerClientTest {
    @BeforeTest
    public void beforeTest() throws Exception {
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

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse touchFileCmdCreateResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout().withAttachStderr().withCmd("touch", "/marker").exec();
        AbstractDockerClientTest.LOG.info("Created exec {}", touchFileCmdCreateResponse.toString());
        MatcherAssert.assertThat(touchFileCmdCreateResponse.getId(), Matchers.not(Matchers.isEmptyString()));
        ExecCreateCmdResponse checkFileCmdCreateResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout().withAttachStderr().withCmd("test", "-e", "/marker").exec();
        AbstractDockerClientTest.LOG.info("Created exec {}", checkFileCmdCreateResponse.toString());
        MatcherAssert.assertThat(checkFileCmdCreateResponse.getId(), Matchers.not(Matchers.isEmptyString()));

        // Check that file does not exist
        InputStream response1 = dockerClient.execStartCmd(container.getId())
                .withExecId(checkFileCmdCreateResponse.getId()).exec();
        asString(response1); // consume

        InspectExecResponse first = dockerClient.inspectExecCmd(checkFileCmdCreateResponse.getId()).exec();
        MatcherAssert.assertThat(first.getExitCode(), Matchers.is(1));

        // Create the file
        InputStream response2 = dockerClient.execStartCmd(container.getId())
                .withExecId(touchFileCmdCreateResponse.getId()).exec();
        asString(response2);

        InspectExecResponse second = dockerClient.inspectExecCmd(touchFileCmdCreateResponse.getId()).exec();
        MatcherAssert.assertThat(second.getExitCode(), Matchers.is(0));

        // Check that file does exist now
        InputStream response3 = dockerClient.execStartCmd(container.getId())
                .withExecId(checkFileCmdCreateResponse.getId()).exec();
        asString(response3);

        InspectExecResponse third = dockerClient.inspectExecCmd(checkFileCmdCreateResponse.getId()).exec();
        MatcherAssert.assertThat(third.getExitCode(), Matchers.is(0));

        // Get container info and check its roundtrip to ensure the consistency
        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
        Assert.assertEquals(containerInfo.getId(), container.getId());
        JSONTestHelper.testRoundTrip(containerInfo);
    }
}
