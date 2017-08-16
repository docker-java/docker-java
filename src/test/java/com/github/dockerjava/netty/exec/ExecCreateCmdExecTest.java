package com.github.dockerjava.netty.exec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class ExecCreateCmdExecTest extends AbstractNettyDockerClientTest {
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
    public void execCreateTest() {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withCmd("touch", "file.log").exec();

        assertThat(execCreateCmdResponse.getId(), not(isEmptyString()));
    }

    @Test
    public void execCreateTestWithEnv() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();
        final List<String> testVariable = Arrays.asList("VARIABLE=dockerJavaTestSuccess");

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withUser("root").withCmd("top")
                .withName(containerName).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withEnv(testVariable)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withCmd("/bin/sh", "-c", "env | grep dockerJavaTestSuccess").exec();

        assertThat(execCreateCmdResponse.getId(), not(isEmptyString()));

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ExecStartResultCallback callback = new ExecStartResultCallback(stdout, stdout);

        dockerClient
                .execStartCmd(execCreateCmdResponse.getId())
                .withDetach(false)
                .exec(callback)
                .awaitCompletion();

        stdout.close();

        assertTrue(stdout.toString().startsWith(testVariable.get(0)));
    }
}
