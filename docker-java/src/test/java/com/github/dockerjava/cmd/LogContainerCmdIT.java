package com.github.dockerjava.cmd;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.utils.LogContainerTestCallback;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
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
        assertThat(container.getId(), not(is(emptyString())));

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
        assertThat(container.getId(), not(is(emptyString())));

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

        assertEquals(StreamType.STDOUT, loggingCallback.getCollectedFrames().get(0).getStreamType());
    }

    @Test
    public void asyncLogNonExistingContainer() throws Exception {

        LogContainerTestCallback loggingCallback = new LogContainerTestCallback() {
            @Override
            public void onError(Throwable throwable) {

                assertEquals(NotFoundException.class.getName(), throwable.getClass().getName());

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
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId())
                .start()
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
        assertThat(container.getId(), not(is(emptyString())));

        int timestamp = (int) (System.currentTimeMillis() / 1000);

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId())
                .start()
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

    @Test(timeout = 10_000)
    public void simultaneousCommands() throws Exception {
        // Create a new client to not affect other tests
        DockerClient client = dockerRule.newClient();
        CreateContainerResponse container = client.createContainerCmd("busybox")
                .withCmd("/bin/sh", "-c", "echo hello world; sleep infinity")
                .exec();

        client.startContainerCmd(container.getId()).exec();

        // Simulate 100 simultaneous connections
        int connections = 100;

        ExecutorService executor = Executors.newFixedThreadPool(connections);
        try {
            List<Frame> firstFrames = new CopyOnWriteArrayList<>();
            executor.invokeAll(
                LongStream.range(0, connections).<Callable<Object>>mapToObj(__ -> {
                    return () -> {
                        return client.logContainerCmd(container.getId())
                            .withStdOut(true)
                            .withFollowStream(true)
                            .exec(new ResultCallback.Adapter<Frame>() {

                                final AtomicBoolean first = new AtomicBoolean(true);

                                @Override
                                public void onNext(Frame object) {
                                    if (first.compareAndSet(true, false)) {
                                        firstFrames.add(object);
                                    }
                                    super.onNext(object);
                                }
                            });
                    };
                }).collect(Collectors.toList())
            );

            await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
                assertThat(firstFrames, hasSize(connections));
            });

            assertThat(firstFrames, everyItem(hasToString("STDOUT: hello world")));
        } finally {
            executor.shutdownNow();
        }
    }

    @Test(timeout = 10_000)
    public void asyncLongDockerLogCmd() throws Exception {
        // Create a new client to not affect other tests
        DockerClient client = dockerRule.newClient();
        String testImage = "icevivek/logreader";

        // Pulling image icevivek/logreader
        try {
            client.inspectImageCmd(testImage).exec();
        } catch (NotFoundException e) {
            LOG.info("Pulling image ");
            // need to block until image is pulled completely
            client.pullImageCmd("icevivek/logreader")
                .withTag("latest")
                .start()
                .awaitCompletion();
        }

        CreateContainerResponse container = client.createContainerCmd("icevivek/logreader")
            .exec();

        client.startContainerCmd(container.getId()).exec();

        // Simulate 100 simultaneous connections
        int connections = 100;

        ExecutorService executor = Executors.newFixedThreadPool(connections);
        try {
            List<Frame> firstFrames = new CopyOnWriteArrayList<>();
            executor.invokeAll(
                LongStream.range(0, connections).<Callable<Object>>mapToObj(__ -> {
                    return () -> {
                        return client.logContainerCmd(container.getId())
                            .withStdOut(true)
                            .withFollowStream(true)
                            .exec(new ResultCallback.Adapter<Frame>() {

                                final AtomicBoolean first = new AtomicBoolean(true);

                                @Override
                                public void onNext(Frame object) {
                                    if (first.compareAndSet(true, false)) {
                                        firstFrames.add(object);
                                    }
                                    super.onNext(object);
                                }
                            });
                    };
                }).collect(Collectors.toList())
            );

            await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
                assertThat(firstFrames, hasSize(connections));
            });

            assertThat(firstFrames.size(), is(187));
        } finally {
            executor.shutdownNow();
        }
    }
}
