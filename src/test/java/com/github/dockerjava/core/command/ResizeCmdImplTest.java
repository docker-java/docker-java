package com.github.dockerjava.core.command;

import static com.github.dockerjava.utils.TestUtils.isSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test(groups = "integration")
public class ResizeCmdImplTest extends AbstractDockerClientTest {
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
    public void execResize() throws Exception {
        //FIXME swarm
        if (isSwarm(dockerClient)) throw new SkipException("FIXME Swarm");

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sh")
                .withName(containerName).withTty(true).withStdinOpen(true).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId())
                .withAttachStdout(true).withTty(true).withCmd("/bin/bash").exec();
        dockerClient.resizeExecCmd(execCreateCmdResponse.getId()).withSize(1024, 1024);
        dockerClient.execStartCmd(execCreateCmdResponse.getId()).exec(
                new ExecStartResultCallback(System.out, System.err)).awaitCompletion();
    }
    
    @Test(groups = "ignoreInCircleCi")
    public void containerResize() throws Exception {
        //FIXME swarm
        if (isSwarm(dockerClient)) throw new SkipException("FIXME Swarm");

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sh")
                .withName(containerName).withTty(true).withStdinOpen(true).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.resizeContainerCmd(container.getId()).withSize(1024, 1024).exec();
        dockerClient.startContainerCmd(container.getId()).exec();
    }
    
}
