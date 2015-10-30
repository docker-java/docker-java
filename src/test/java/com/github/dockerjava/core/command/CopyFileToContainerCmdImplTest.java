package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

public class CopyFileToContainerCmdImplTest extends AbstractDockerClientTest {
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
    public void copyToContainer() throws Exception {
        // TODO extract this into a shared method
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withName("docker-java-itest-copyFromContainer").withCmd("touch", "/copyFromContainer").exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        dockerClient.copyFileToContainerCmd(container.getId(), "src/test/resources/testReadFile").exec();
        try (InputStream response = dockerClient.copyFileFromContainerCmd(container.getId(), "testReadFile").exec()) {
            boolean bytesAvailable = response.available() > 0;
            assertTrue(bytesAvailable, "The file was not copied to the container.");
        }
    }

    @Test
    public void copyToNonExistingContainer() throws Exception {
        try {
            dockerClient.copyFileFromContainerCmd("non-existing", "/test").exec();
            fail("expected NotFoundException");
        } catch (NotFoundException ignored) {
        }
    }

}
