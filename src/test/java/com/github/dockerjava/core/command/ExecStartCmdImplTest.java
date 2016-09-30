package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test(groups = "integration")
public class ExecStartCmdImplTest extends AbstractDockerClientTest {
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
    public void execStart() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true).withCmd("touch", "/execStartTest.log").withUser("root").exec();
        dockerClient.execStartCmd(execCreateCmdResponse.getId()).exec(
                new ExecStartResultCallback(System.out, System.err)).awaitCompletion();

        InputStream response = dockerClient.copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();
        Boolean bytesAvailable = response.available() > 0;
        assertTrue(bytesAvailable, "The file was not copied from the container.");

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test(groups = "ignoreInCircleCi")
    public void execStartAttached() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true).withCmd("touch", "/execStartTest.log").exec();
        dockerClient.execStartCmd(execCreateCmdResponse.getId()).withDetach(false).withTty(true)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();

        InputStream response = dockerClient.copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();
        Boolean bytesAvailable = response.available() > 0;
        assertTrue(bytesAvailable, "The file was not copied from the container.");

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }
    
    @Test(groups = "ignoreInCircleCi", expectedExceptions = NotFoundException.class)
    public void execStartWithNonExistentUser() throws Exception {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true).withCmd("touch", "/execStartTest.log").withUser("NonExistentUser").exec();
        dockerClient.execStartCmd(execCreateCmdResponse.getId()).withDetach(false).withTty(true)
                .exec(new ExecStartResultCallback(System.out, System.err)).awaitCompletion();

        dockerClient.copyArchiveFromContainerCmd(container.getId(), "/execStartTest.log").exec();
    }
}
