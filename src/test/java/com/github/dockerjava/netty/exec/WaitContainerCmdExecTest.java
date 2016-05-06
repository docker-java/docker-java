package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.WaitResponse;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Test(groups = "integration")
public class WaitContainerCmdExecTest extends AbstractNettyDockerClientTest {

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
    public void testWaitContainer() throws DockerException {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("true").exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback())
                .awaitStatusCode();
        LOG.info("Container exit code: {}", exitCode);

        assertThat(exitCode, equalTo(0));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(false)));
        assertThat(inspectContainerResponse.getState().getExitCode(), is(equalTo(exitCode)));
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testWaitNonExistingContainer() throws DockerException {

        WaitContainerResultCallback callback = new WaitContainerResultCallback() {
            public void onNext(WaitResponse waitResponse) {
                fail("expected NotFoundException");
            }
        };

        dockerClient.waitContainerCmd("non-existing").exec(callback).awaitStatusCode();
    }

    @Test
    public void testWaitContainerAbort() throws Exception {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        WaitContainerResultCallback callback = dockerClient.waitContainerCmd(container.getId()).exec(
                new WaitContainerResultCallback());

        Thread.sleep(5000);

        System.err.println("closing callback");

        callback.close();

        dockerClient.killContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();
        LOG.info("Container Inspect: {}", inspectContainerResponse.toString());

        assertThat(inspectContainerResponse.getState().getRunning(), is(equalTo(false)));
    }

    @Test
    public void testWaitContainerTimeout() throws Exception {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "10").exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        WaitContainerResultCallback callback = dockerClient.waitContainerCmd(container.getId()).exec(
                new WaitContainerResultCallback());
        try {
            callback.awaitStatusCode(100, TimeUnit.MILLISECONDS);
            fail("Should throw exception on timeout.");
        } catch(DockerClientException e){
            LOG.info(e.getMessage());
        }
    }
}
