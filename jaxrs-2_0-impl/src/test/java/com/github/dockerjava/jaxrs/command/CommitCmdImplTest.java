package com.github.dockerjava.jaxrs.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

import java.lang.reflect.Method;

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
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class CommitCmdImplTest extends AbstractDockerClientTest {

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
    public void commit() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("touch", "/test").exec();

        AbstractDockerClientTest.LOG.info("Created container: {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        AbstractDockerClientTest.LOG.info("Commiting container: {}", container.toString());
        String imageId = dockerClient.commitCmd(container.getId()).exec();

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        AbstractDockerClientTest.LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        MatcherAssert.assertThat(inspectImageResponse, HasFieldWithValue.hasField("container", Matchers.startsWith(container.getId())));
        MatcherAssert.assertThat(inspectImageResponse.getContainerConfig().getImage(), Matchers.equalTo("busybox"));

        InspectImageResponse busyboxImg = dockerClient.inspectImageCmd("busybox").exec();

        MatcherAssert.assertThat(inspectImageResponse.getParent(), Matchers.equalTo(busyboxImg.getId()));
    }

    @Test
    public void commitNonExistingContainer() throws DockerException {
        try {
            dockerClient.commitCmd("non-existent").exec();
            Assert.fail("expected NotFoundException");
        } catch (NotFoundException e) {
        }
    }

}
