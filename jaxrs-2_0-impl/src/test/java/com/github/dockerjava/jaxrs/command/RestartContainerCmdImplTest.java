package com.github.dockerjava.jaxrs.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class RestartContainerCmdImplTest extends AbstractDockerClientTest {

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
        AbstractDockerClientTest.LOG.info("Created container: {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        AbstractDockerClientTest.LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        String startTime = inspectContainerResponse.getState().getStartedAt();

        dockerClient.restartContainerCmd(container.getId()).withtTimeout(2).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container.getId()).exec();
        AbstractDockerClientTest.LOG.info("Container Inspect After Restart: {}", inspectContainerResponse2.toString());

        String startTime2 = inspectContainerResponse2.getState().getStartedAt();

        MatcherAssert.assertThat(startTime, Matchers.not(Matchers.equalTo(startTime2)));

        MatcherAssert.assertThat(inspectContainerResponse.getState().isRunning(), Matchers.is(Matchers.equalTo(true)));

        dockerClient.killContainerCmd(container.getId()).exec();
    }

    @Test
    public void restartNonExistingContainer() throws DockerException, InterruptedException {
        try {
            dockerClient.restartContainerCmd("non-existing").exec();
            Assert.fail("expected NotFoundException");
        } catch (NotFoundException e) {
        }

    }

}
