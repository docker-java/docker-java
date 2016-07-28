package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.github.dockerjava.api.model.Capability.MKNOD;
import static com.github.dockerjava.api.model.Capability.NET_ADMIN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;


import java.lang.reflect.Method;


import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

@Test(groups = "integration")
public class RemoveContainerCmdImplTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(RemoveContainerCmdImplTest.class);

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
    public void removeContainer() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd(BUSYBOX_IMAGE).withCmd("true").exec();

        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback()).awaitStatusCode();

        LOG.info("Removing container: {}", container.getId());
        dockerClient.removeContainerCmd(container.getId()).exec();

        List<Container> containers2 = dockerClient.listContainersCmd().withShowAll(true).exec();

        Matcher matcher = not(hasItem(hasField("id", startsWith(container.getId()))));
        assertThat(containers2, matcher);

    }

    @Test(expectedExceptions = NotFoundException.class)
    public void removeNonExistingContainer() throws DockerException {
        dockerClient.removeContainerCmd("non-existing").exec();
    }

    @Test(groups = "ignoreInCircleCi", expectedExceptions = ConflictException.class)
    public void removePausedContainer() {
        CreateContainerResponse container = dockerClient.createContainerCmd(BUSYBOX_IMAGE).withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        dockerClient.pauseContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse1 = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse1.getState().getPaused(), is(true));

        dockerClient.removeContainerCmd(container.getId()).exec();
    }

    @Test(groups = "ignoreInCircleCi", expectedExceptions = ConflictException.class)
    public void removeRunningContainer() {
        CreateContainerResponse container = dockerClient.createContainerCmd(BUSYBOX_IMAGE).withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        dockerClient.removeContainerCmd(container.getId()).exec();
    }
}
