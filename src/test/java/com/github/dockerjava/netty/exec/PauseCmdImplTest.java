package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
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
public class PauseCmdImplTest extends AbstractNettyDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(PauseCmdImplTest.class);

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
    public void pauseRunningContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        ContainerUtils.startContainer(dockerClient, container);

        ContainerUtils.pauseContainer(dockerClient, container);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void pauseNonExistingContainer() {

        dockerClient.pauseContainerCmd("non-existing").exec();
    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void pauseStoppedContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        ContainerUtils.startContainer(dockerClient, container);

        ContainerUtils.stopContainer(dockerClient, container);

        dockerClient.pauseContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void pausePausedContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        ContainerUtils.startContainer(dockerClient, container);

        ContainerUtils.pauseContainer(dockerClient, container);

        dockerClient.pauseContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = InternalServerErrorException.class)
    public void pauseCreatedContainer() {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.pauseContainerCmd(container.getId()).exec();
    }
}
