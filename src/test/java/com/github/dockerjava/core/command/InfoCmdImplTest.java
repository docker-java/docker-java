package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class InfoCmdImplTest extends AbstractDockerClientTest {

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
    public void info() throws DockerException {
        // Make sure that there is at least one container for the assertion
        // TODO extract this into a shared method
        if (dockerClient.listContainersCmd().withShowAll(true).exec().size() == 0) {
            CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                    .withName("docker-java-itest-info").withCmd("touch", "/test").exec();

            LOG.info("Created container: {}", container);
            assertThat(container.getId(), not(isEmptyOrNullString()));

            dockerClient.startContainerCmd(container.getId()).exec();
        }

        Info dockerInfo = dockerClient.infoCmd().exec();
        LOG.info(dockerInfo.toString());

        assertTrue(dockerInfo.toString().contains("containers"));
        assertTrue(dockerInfo.toString().contains("images"));
        assertTrue(dockerInfo.toString().contains("debug"));

        assertTrue(dockerInfo.getContainers() > 0);
        assertTrue(dockerInfo.getImages() > 0);
        assertTrue(dockerInfo.getNFd() > 0);
        assertTrue(dockerInfo.getNGoroutines() > 0);
        assertTrue(dockerInfo.getNCPU() > 0);
    }
}
