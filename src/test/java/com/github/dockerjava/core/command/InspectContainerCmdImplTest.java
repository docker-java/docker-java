package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;
import java.security.SecureRandom;

import com.github.dockerjava.api.command.InspectContainerCmd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class InspectContainerCmdImplTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(InspectContainerCmdImplTest.class);

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

    @Test()
    public void inspectContainer() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());

    }

    @Test()
    public void inspectContainerWithSize() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerCmd command = dockerClient.inspectContainerCmd(container.getId()).withSize(true);
        assertTrue(command.getSize());
        InspectContainerResponse containerInfo  = command.exec();
        assertEquals(containerInfo.getId(), container.getId());
        assertNotNull(containerInfo.getSizeRootFs());
        assertTrue(containerInfo.getSizeRootFs().intValue() > 0 );
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void inspectNonExistingContainer() throws DockerException {
        dockerClient.inspectContainerCmd("non-existing").exec();
    }

    @Test
    public void inspectContainerRestartCount() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getRestartCount(), equalTo(0));
    }

    @Test
    public void inspectContainerNetworkSettings() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("env").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertFalse(inspectContainerResponse.getNetworkSettings().getHairpinMode());
    }
}
