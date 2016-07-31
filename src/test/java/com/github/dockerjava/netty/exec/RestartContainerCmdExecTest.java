package com.github.dockerjava.netty.exec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class RestartContainerCmdExecTest extends AbstractNettyDockerClientTest {

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

    @Test
    public void restartContainer() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        String startTime = inspectContainerResponse.getState().getStartedAt();

        dockerClient.restartContainerCmd(container.getId()).withtTimeout(2).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect After Restart: {}", inspectContainerResponse2.toString());

        String startTime2 = inspectContainerResponse2.getState().getStartedAt();

        assertThat(startTime, not(equalTo(startTime2)));

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(true)));

        dockerClient.killContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void restartNonExistingContainer() throws DockerException, InterruptedException {

        dockerClient.restartContainerCmd("non-existing").exec();
    }

}
