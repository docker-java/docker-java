package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.utils.ContainerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

@Test(groups = {"integration", "ignoreInCircleCi"})
public class UnpauseCmdImplTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(UnpauseCmdImplTest.class);

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
    public void unpausePausedContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        ContainerUtils.startContainer(dockerClient, container);

        ContainerUtils.pauseContainer(dockerClient, container);

        ContainerUtils.unpauseContainer(dockerClient, container);
    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void unpauseRunningContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        ContainerUtils.startContainer(dockerClient, container);

        dockerClient.unpauseContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void unpauseStoppedContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        ContainerUtils.startContainer(dockerClient, container);

        ContainerUtils.stopContainer(dockerClient, container);

        dockerClient.unpauseContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void unpauseNonExistingContainer() {

        dockerClient.unpauseContainerCmd("non-existing").exec();
    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void unpauseCreatedContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.unpauseContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void unpauseUnpausedContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        ContainerUtils.startContainer(dockerClient, container);

        ContainerUtils.pauseContainer(dockerClient, container);

        dockerClient.unpauseContainerCmd(container.getId()).exec();
        dockerClient.unpauseContainerCmd(container.getId()).exec();
    }
}
