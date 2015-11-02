package com.github.dockerjava.jaxrs.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
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

import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;

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
    public void asyncLogContainer() throws Exception {

        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("/bin/echo", snippet)
                .exec();

        AbstractDockerClientTest.LOG.info("Created container: {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(exitCode, Matchers.equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback();

        // this essentially test the since=0 case
        dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec(loggingCallback);

        loggingCallback.awaitCompletion();

        Assert.assertTrue(loggingCallback.toString().contains(snippet));
    }

    @Test
    public void asyncLogNonExistingContainer() throws Exception {

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback() {
            @Override
            public void onError(Throwable throwable) {

                Assert.assertEquals(throwable.getClass().getName(), NotFoundException.class.getName());

                try {
                    // close the callback to prevent the call to onFinish
                    close();
                } catch (IOException e) {
                    throw new RuntimeException();
                }

                super.onError(throwable);
            }

            public void onComplete() {
                super.onComplete();
                Assert.fail("expected NotFoundException");
            };
        };

        dockerClient.logContainerCmd("non-existing").withStdErr().withStdOut().exec(loggingCallback).awaitCompletion();
    }

    @Test
    public void asyncMultipleLogContainer() throws Exception {

        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("/bin/echo", snippet)
                .exec();

        AbstractDockerClientTest.LOG.info("Created container: {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(exitCode, Matchers.equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec(loggingCallback);

        loggingCallback.close();

        loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec(loggingCallback);

        loggingCallback.close();

        loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().exec(loggingCallback);

        loggingCallback.awaitCompletion();

        Assert.assertTrue(loggingCallback.toString().contains(snippet));
    }

    @Test
    public void asyncLogContainerWithSince() throws Exception {
        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("/bin/echo", snippet)
                .exec();

        AbstractDockerClientTest.LOG.info("Created container: {}", container.toString());
        MatcherAssert.assertThat(container.getId(), Matchers.not(Matchers.isEmptyString()));

        int timestamp = (int) (System.currentTimeMillis() / 1000);

        dockerClient.startContainerCmd(container.getId()).exec();

        int exitCode = dockerClient.waitContainerCmd(container.getId()).exec();

        MatcherAssert.assertThat(exitCode, Matchers.equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback();

        dockerClient.logContainerCmd(container.getId()).withStdErr().withStdOut().withSince(timestamp)
                .exec(loggingCallback);

        loggingCallback.awaitCompletion();

        Assert.assertFalse(loggingCallback.toString().contains(snippet));
    }

}
