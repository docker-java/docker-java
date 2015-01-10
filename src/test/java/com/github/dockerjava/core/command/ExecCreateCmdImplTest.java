package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.security.SecureRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

@Test(groups = "integration")
public class ExecCreateCmdImplTest extends AbstractDockerClientTest {
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

    @Test
    public void execCreateTest() {
        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient
                .createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(container.getId()).withCmd("touch","file.log").exec();

        assertThat(execCreateCmdResponse.getId(), not(isEmptyString()));
    }
}
