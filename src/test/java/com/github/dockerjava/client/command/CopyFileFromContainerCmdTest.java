package com.github.dockerjava.client.command;

import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.*;

import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class CopyFileFromContainerCmdTest extends AbstractDockerClientTest {

    @BeforeTest
    public void beforeTest() {
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
    public void copyFromContainer() {
        // TODO extract this into a shared method
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withName("docker-java-itest-copyFromContainer")
                .withCmd("touch", "/test")
                .exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        dockerClient.startContainerCmd(container.getId()).exec();
        tmpContainers.add(container.getId());

        Response response = dockerClient.copyFileFromContainerCmd(container.getId(), "/test").exec();
        assertTrue(response.getStatus() == 200 && response.hasEntity(), "The file was not copied from the container.");
    }
}
