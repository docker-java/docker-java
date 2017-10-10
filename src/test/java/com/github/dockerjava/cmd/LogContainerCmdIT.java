package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import com.github.dockerjava.utils.LogContainerTestCallback;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogContainerCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(LogContainerCmdIT.class);

    @Test
    public void asyncLogContainerWithTtyEnabled() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("/bin/sh", "-c", "while true; do echo hello; sleep 1; done")
                .withTty(true)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId())
            .exec();

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback(true);

        // this essentially test the since=0 case
        dockerRule.getClient().logContainerCmd(container.getId())
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

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("/bin/sh", "-c", "while true; do echo hello; sleep 1; done")
                .withTty(false)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId())
            .exec();

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback(true);

        // this essentially test the since=0 case
        dockerRule.getClient().logContainerCmd(container.getId())
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
                throw new AssertionError("expected NotFoundException");
            };
        };

        dockerRule.getClient().logContainerCmd("non-existing").withStdErr(true).withStdOut(true).exec(loggingCallback)
                .awaitCompletion();
    }

    @Test
    public void asyncMultipleLogContainer() throws Exception {

        String snippet = "hello world";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("/bin/echo", snippet)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode();

        assertThat(exitCode, equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback();

        dockerRule.getClient().logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .exec(loggingCallback);

        loggingCallback.close();

        loggingCallback = new LogContainerTestCallback();

        dockerRule.getClient().logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .exec(loggingCallback);

        loggingCallback.close();

        loggingCallback = new LogContainerTestCallback();

        dockerRule.getClient().logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .exec(loggingCallback);

        loggingCallback.awaitCompletion();

        assertTrue(loggingCallback.toString().contains(snippet));
    }

    @Test
    public void asyncLogContainerWithSince() throws Exception {
        String snippet = "hello world";

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("/bin/echo", snippet)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        int timestamp = (int) (System.currentTimeMillis() / 1000);

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId())
                .exec(new WaitContainerResultCallback())
                .awaitStatusCode();

        assertThat(exitCode, equalTo(0));

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback();

        dockerRule.getClient().logContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withSince(timestamp)
                .exec(loggingCallback);

        loggingCallback.awaitCompletion();

        assertThat(loggingCallback.toString(), containsString(snippet));
    }
}
