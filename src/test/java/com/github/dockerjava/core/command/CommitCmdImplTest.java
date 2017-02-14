package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

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

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        LOG.info("Committing container: {}", container.toString());
        String imageId = dockerClient.commitCmd(container.getId()).exec();

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        assertThat(inspectImageResponse, hasField("container", startsWith(container.getId())));
        assertThat(inspectImageResponse.getContainerConfig().getImage(), equalTo("busybox"));

        InspectImageResponse busyboxImg = dockerClient.inspectImageCmd("busybox").exec();

        assertThat(inspectImageResponse.getParent(), equalTo(busyboxImg.getId()));
    }

    @Test
    public void commitWithLabels() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("touch", "/test").exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        LOG.info("Committing container: {}", container.toString());
        Map<String, String> labels = ImmutableMap.of("label1", "abc", "label2", "123");
        String imageId = dockerClient.commitCmd(container.getId()).withLabels(labels).exec();

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());
        Map<String, String> responseLabels = inspectImageResponse.getContainerConfig().getLabels();
        assertThat(responseLabels.get("label1"), equalTo("abc"));
        assertThat(responseLabels.get("label2"), equalTo("123"));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void commitNonExistingContainer() throws DockerException {

        dockerClient.commitCmd("non-existent").exec();
    }
}
