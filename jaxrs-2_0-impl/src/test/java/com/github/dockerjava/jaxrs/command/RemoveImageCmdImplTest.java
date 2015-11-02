package com.github.dockerjava.jaxrs.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

import java.lang.reflect.Method;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;

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

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        LOG.info("Committing container {}", container.toString());
        String imageId = dockerClient.commitCmd(container.getId()).exec();

        dockerClient.stopContainerCmd(container.getId()).exec();
        dockerClient.killContainerCmd(container.getId()).exec();
        dockerClient.removeContainerCmd(container.getId()).exec();

        LOG.info("Removing image: {}", imageId);
        dockerClient.removeImageCmd(imageId).exec();

        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();

        Matcher matcher = Matchers.not(Matchers.hasItem(HasFieldWithValue.hasField("id", Matchers.startsWith(imageId))));
        MatcherAssert.assertThat(containers, matcher);
    }

    @Test
    public void removeNonExistingImage() throws DockerException, InterruptedException {
        try {
            dockerClient.removeImageCmd("non-existing").exec();
            Assert.fail("expected NotFoundException");
        } catch (NotFoundException e) {
        }

    }

}
