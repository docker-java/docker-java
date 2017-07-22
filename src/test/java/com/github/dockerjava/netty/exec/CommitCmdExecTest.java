package com.github.dockerjava.netty.exec;

import static com.github.dockerjava.utils.TestUtils.isSwarm;
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
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class CommitCmdExecTest extends AbstractNettyDockerClientTest {

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
        if (isSwarm(dockerClient)) throw new SkipException("FIXME Swarm");

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
        if (isSwarm(dockerClient)) throw new SkipException("FIXME Swarm");

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("touch", "/test")
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        LOG.info("Committing container: {}", container.toString());
        Map<String, String> labels = ImmutableMap.of("nettyLabel1", "abc", "nettyLabel2", "123");
        String imageId = dockerClient.commitCmd(container.getId()).withLabels(labels).exec();

        InspectImageResponse inspectImageResponse = dockerClient.inspectImageCmd(imageId).exec();
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());
        Map<String, String> responseLabels = inspectImageResponse.getContainerConfig().getLabels();
        assertThat(responseLabels.get("nettyLabel1"), equalTo("abc"));
        assertThat(responseLabels.get("nettyLabel2"), equalTo("123"));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void commitNonExistingContainer() throws DockerException {

        dockerClient.commitCmd("non-existent").exec();
    }

}
