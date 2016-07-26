package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Test(groups = "integration")
public class RestartContainerCmdImplTest extends AbstractDockerClientTest {

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
    public void restartContainer() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd(BUSYBOX_IMAGE).withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        String startTime = inspectContainerResponse.getState().getStartedAt();

        dockerClient.restartContainerCmd(container.getId()).withtTimeout(2).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect After Restart: {}", inspectContainerResponse2.toString());

        String startTime2 = inspectContainerResponse2.getState().getStartedAt();

        assertThat(startTime, not(equalTo(startTime2)));

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(true)));

        dockerClient.killContainerCmd(container.getId()).exec();
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void restartNonExistingContainer() throws DockerException, InterruptedException {

        dockerClient.restartContainerCmd("non-existing").exec();
    }

    @Test
    public void restartStoppedContainer() {
        final RemoteApiVersion apiVersion = getVersion(dockerClient);

        CreateContainerResponse container = dockerClient.createContainerCmd(BUSYBOX_IMAGE).withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        LOG.info("Stopping container: {}", container.getId());
        dockerClient.stopContainerCmd(container.getId()).withTimeout(2).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(false)));

        final Integer exitCode = inspectContainerResponse.getState().getExitCode();
        if (apiVersion.equals(VERSION_1_22)) {
            assertThat(exitCode, is(0));
        } else {
            assertThat(exitCode, not(0));
        }

        LOG.info("Restarting stopped container: {}", container.toString());
        String startTime = inspectContainerResponse.getState().getStartedAt();

        dockerClient.restartContainerCmd(container.getId()).withtTimeout(2).exec();

        InspectContainerResponse inspectContainerResponse2 = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect After Restart: {}", inspectContainerResponse2.toString());

        String startTime2 = inspectContainerResponse2.getState().getStartedAt();
        assertThat(startTime, not(equalTo(startTime2)));
        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(false)));
        dockerClient.killContainerCmd(container.getId()).exec();
    }

}
