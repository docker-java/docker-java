package com.github.dockerjava.netty.exec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.lang.reflect.Method;
import java.security.SecureRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.InspectExecResponse;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.github.dockerjava.netty.AbstractDockerClientTest;
import com.github.dockerjava.test.serdes.JSONTestHelper;

@Test(groups = "integration")
public class InspectContainerCmdExecTest extends AbstractDockerClientTest {

    public static final Logger LOG = LoggerFactory.getLogger(InspectContainerCmdExecTest.class);

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

    @Test()
    public void inspectContainer() throws DockerException {

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        InspectContainerResponse containerInfo = dockerClient.inspectContainerCmd(container.getId()).exec();
        assertEquals(containerInfo.getId(), container.getId());

    }

    @Test
    public void inspectNonExistingContainer() throws DockerException {

        try {
            dockerClient.inspectContainerCmd("non-existing").exec();
            fail("expected NotFoundException");
        } catch (NotFoundException e) {
        }
    }

}
