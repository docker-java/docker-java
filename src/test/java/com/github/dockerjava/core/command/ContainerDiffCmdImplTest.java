package com.github.dockerjava.core.command;

import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

import java.lang.reflect.Method;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.ChangeLog;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class ContainerDiffCmdImplTest extends AbstractDockerClientTest {

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
    public void testContainerDiff() throws DockerException {
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("touch", "/test").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback())
                .awaitStatusCode();
        assertThat(exitCode, equalTo(0));

        List<ChangeLog> filesystemDiff = dockerClient.containerDiffCmd(container.getId()).exec();
        LOG.info("Container DIFF: {}", filesystemDiff.toString());

        assertThat(filesystemDiff.size(), equalTo(1));
        ChangeLog testChangeLog = selectUnique(filesystemDiff, hasField("path", equalTo("/test")));

        assertThat(testChangeLog, hasField("path", equalTo("/test")));
        assertThat(testChangeLog, hasField("kind", equalTo(1)));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testContainerDiffWithNonExistingContainer() throws DockerException {

        dockerClient.containerDiffCmd("non-existing").exec();
    }
}
