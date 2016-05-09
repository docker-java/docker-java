package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;
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

@Test(groups = "integration")
public class RenameContainerCmdImplTest extends AbstractDockerClientTest {
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
    public void renameContainer() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        String name1 = inspectContainerResponse.getName();

        dockerClient.renameContainerCmd(container.getId())
                .withName(getClass().getCanonicalName() + "renameContainer")
                .exec();

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect After Rename: {}", inspectContainerResponse2.toString());

        String name2 = inspectContainerResponse2.getName();

        assertNotEquals(name1, name2);

        dockerClient.killContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void renameExistingContainer() throws DockerException, InterruptedException {
        dockerClient.renameContainerCmd("non-existing")
                .withName(getClass().getCanonicalName() + "renameExistingContainer")
                .exec();
    }
}
