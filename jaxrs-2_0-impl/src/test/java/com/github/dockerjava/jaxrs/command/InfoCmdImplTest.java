package com.github.dockerjava.jaxrs.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;

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

            AbstractDockerClientTest.LOG.info("Created container: {}", container);
            MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyOrNullString()));

            dockerClient.startContainerCmd(container.getId()).exec();
        }

        Info dockerInfo = dockerClient.infoCmd().exec();
        AbstractDockerClientTest.LOG.info(dockerInfo.toString());

        Assert.assertTrue(dockerInfo.toString().contains("containers"));
        Assert.assertTrue(dockerInfo.toString().contains("images"));
        Assert.assertTrue(dockerInfo.toString().contains("debug"));

        Assert.assertTrue(dockerInfo.getContainers() > 0);
        Assert.assertTrue(dockerInfo.getImages() > 0);
        Assert.assertTrue(dockerInfo.getNFd() > 0);
        Assert.assertTrue(dockerInfo.getNGoroutines() > 0);
        Assert.assertTrue(dockerInfo.getNCPU() > 0);
    }
}
