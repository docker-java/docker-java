package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

@Test(groups = "integration")
public class RemoveImageCmdImplTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(RemoveImageCmdImplTest.class);

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
    public void removeImage() throws DockerException, InterruptedException {

        CreateContainerResponse container = dockerClient.createContainerCmd(BUSYBOX_IMAGE).withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        LOG.info("Committing container {}", container.toString());
        String imageId = dockerClient.commitCmd(container.getId()).exec();

        dockerClient.stopContainerCmd(container.getId()).exec();
        dockerClient.removeContainerCmd(container.getId()).exec();

        LOG.info("Removing image: {}", imageId);
        dockerClient.removeImageCmd(imageId).exec();

        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();

        Matcher matcher = not(hasItem(hasField("id", startsWith(imageId))));
        assertThat(containers, matcher);
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void removeNonExistingImage() throws DockerException, InterruptedException {

        dockerClient.removeImageCmd("non-existing").exec();
    }

}
