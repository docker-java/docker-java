package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class LogContainerCmdImplTest extends AbstractDockerClientTest {

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
    public void asyncLogContainerWithTtyEnabled() throws Exception {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("/bin/sh", "-c", "while true; do echo hello; sleep 1; done")
                .withTty(true)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId())
            .exec();

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback(true);

        // this essentially test the since=0 case
        dockerClient.logContainerCmd(container.getId())
            .withStdErr(true)
            .withStdOut(true)
            .withFollowStream(true)
            .withTailAll()
            .exec(loggingCallback);

        loggingCallback.awaitCompletion(3, TimeUnit.SECONDS);

        assertTrue(loggingCallback.toString().contains("hello"));

        assertEquals(loggingCallback.getCollectedFrames().get(0).getStreamType(), StreamType.RAW);
    }

    @Test
    public void asyncLogContainerWithTtyDisabled() throws Exception {

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("/bin/sh", "-c", "while true; do echo hello; sleep 1; done")
                .withTty(false)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId())
            .exec();

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback(true);

        // this essentially test the since=0 case
        dockerClient.logContainerCmd(container.getId())
            .withStdErr(true)
            .withStdOut(true)
            .withFollowStream(true)
            .withTailAll()
            .exec(loggingCallback);

        loggingCallback.awaitCompletion(3, TimeUnit.SECONDS);

        assertTrue(loggingCallback.toString().contains("hello"));

        assertEquals(loggingCallback.getCollectedFrames().get(0).getStreamType(), StreamType.STDOUT);
    }

    @Test
    public void asyncLogNonExistingContainer() throws Exception {

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback() {
            @Override
            public void onError(Throwable throwable) {

                assertEquals(throwable.getClass().getName(), NotFoundException.class.getName());

                try {
                    // close the callback to prevent the call to onComplete
                    close();
                } catch (IOException e) {
                    throw new RuntimeException();
                }

                super.onError(throwable);
            }

            public void onComplete() {
                super.onComplete();
                fail("expected NotFoundException");
            };
        };

        dockerClient.logContainerCmd("non-existing").withStdErr(true).withStdOut(true).exec(loggingCallback)
                .awaitCompletion();
    }

    @Test
    public void asyncMultipleLogContainer() throws Exception {

        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("/bin/echo", snippet)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode();

        assertThat(exitCode, equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .exec(loggingCallback);

        loggingCallback.close();

        loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .exec(loggingCallback);

        loggingCallback.close();

        loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .exec(loggingCallback);

        loggingCallback.awaitCompletion();

        assertTrue(loggingCallback.toString().contains(snippet));
    }

    @Test
    public void asyncLogContainerWithSince() throws Exception {
        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("/bin/echo", snippet)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        int timestamp = (int) (System.currentTimeMillis() / 1000);

        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode();

        assertThat(exitCode, equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withSince(timestamp)
                .exec(loggingCallback);

        loggingCallback.awaitCompletion();

        assertThat(loggingCallback.toString(), containsString(snippet));
    }
}
