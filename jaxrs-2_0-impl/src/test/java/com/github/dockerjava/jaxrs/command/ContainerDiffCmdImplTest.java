package com.github.dockerjava.jaxrs.command;

import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

import java.lang.reflect.Method;
import java.util.List;

import ch.lambdaj.Lambda;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testinfected.hamcrest.jpa.HasFieldWithValue;
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
import com.github.dockerjava.api.model.ChangeLog;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;

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
        AbstractDockerClientTest.LOG.info("Created container: {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();
        MatcherAssert.assertThat(exitCode, Matchers.equalTo(0));

        List<ChangeLog> filesystemDiff = dockerClient.containerDiffCmd(container.getId()).exec();
        AbstractDockerClientTest.LOG.info("Container DIFF: {}", filesystemDiff.toString());

        MatcherAssert.assertThat(filesystemDiff.size(), Matchers.equalTo(1));
        ChangeLog testChangeLog = Lambda.selectUnique(filesystemDiff, HasFieldWithValue.hasField("path", Matchers.equalTo("/test")));

        MatcherAssert.assertThat(testChangeLog, HasFieldWithValue.hasField("path", Matchers.equalTo("/test")));
        MatcherAssert.assertThat(testChangeLog, HasFieldWithValue.hasField("kind", Matchers.equalTo(1)));
    }

    @Test
    public void testContainerDiffWithNonExistingContainer() throws DockerException {
        try {
            dockerClient.containerDiffCmd("non-existing").exec();
            Assert.fail("expected NotFoundException");
        } catch (NotFoundException e) {
        }
    }

}
