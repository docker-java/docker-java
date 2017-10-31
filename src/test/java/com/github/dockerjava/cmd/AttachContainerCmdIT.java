package com.github.dockerjava.cmd;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.command.AttachContainerResultCallback;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

/**
 * @author Kanstantsin Shautsou
 */
public class AttachContainerCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(AttachContainerCmdIT.class);


    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void attachContainerWithStdin() throws Exception {
        DockerClient dockerClient = dockerRule.getClient();

        assumeThat(getFactoryType(), is(FactoryType.NETTY));

        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox")
                .withCmd("/bin/sh", "-c", "sleep 1 && read line && echo $line")
                .withTty(false)
                .withStdinOpen(true)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container.getId()).exec();

        assertThat(inspectContainerResponse.getState().getRunning(), is(true));

        AttachContainerTestCallback callback = new AttachContainerTestCallback() {
            @Override
            public void onNext(Frame frame) {
                assertEquals(frame.getStreamType(), StreamType.STDOUT);
                super.onNext(frame);
            }
        };

        PipedOutputStream out = new PipedOutputStream();
        PipedInputStream in = new PipedInputStream(out);

        dockerClient.attachContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .withStdIn(in)
                .exec(callback);

        out.write((snippet + "\n").getBytes());
        out.flush();

        callback.awaitCompletion(15, SECONDS);
        callback.close();

        assertThat(callback.toString(), containsString(snippet));
    }

    @Test
    public void attachContainerWithoutTTY() throws Exception {
        DockerClient dockerClient = dockerRule.getClient();

        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd(DEFAULT_IMAGE)
                .withCmd("echo", snippet)
                .withTty(false)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        AttachContainerTestCallback callback = new AttachContainerTestCallback() {
            @Override
            public void onNext(Frame frame) {
                assertThat(frame.getStreamType(), equalTo(StreamType.STDOUT));
                super.onNext(frame);
            };
        };

        dockerClient.attachContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .withLogs(true)
                .exec(callback)
                .awaitCompletion(30, TimeUnit.SECONDS);
        callback.close();

        assertThat(callback.toString(), containsString(snippet));
    }

    @Test
    public void attachContainerWithTTY() throws Exception {
        DockerClient dockerClient = dockerRule.getClient();

        File baseDir = new File(Thread.currentThread().getContextClassLoader()
                .getResource("attachContainerTestDockerfile").getFile());

        String imageId = dockerRule.buildImage(baseDir);

        CreateContainerResponse container = dockerClient.createContainerCmd(imageId).withTty(true).exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        AttachContainerTestCallback callback = new AttachContainerTestCallback() {
            @Override
            public void onNext(Frame frame) {
                assertThat(frame.getStreamType(), equalTo(StreamType.RAW));
                super.onNext(frame);
            };
        };

        dockerClient.attachContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .exec(callback)
                .awaitCompletion(15, TimeUnit.SECONDS);
        callback.close();

        LOG.debug("log: {}", callback.toString());

        // HexDump.dump(collectFramesCallback.toString().getBytes(), 0, System.out, 0);
        assertThat(callback.toString(), containsString("stdout\r\nstderr"));
    }

    @Test
    public void attachContainerStdinUnsupported() throws Exception {

        DockerClient dockerClient = dockerRule.getClient();
        if (getFactoryType() == FactoryType.JERSEY) {
            expectedException.expect(UnsupportedOperationException.class);
        }

        String snippet = "hello world";

        CreateContainerResponse container = dockerClient.createContainerCmd(DEFAULT_IMAGE)
                .withCmd("echo", snippet)
                .withTty(false)
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        AttachContainerTestCallback callback = new AttachContainerTestCallback() {
            @Override
            public void onNext(Frame frame) {
                assertThat(frame.getStreamType(), equalTo(StreamType.STDOUT));
                super.onNext(frame);
            };
        };

        InputStream stdin = new ByteArrayInputStream("".getBytes());

        dockerClient.attachContainerCmd(container.getId())
                .withStdErr(true)
                .withStdOut(true)
                .withFollowStream(true)
                .withLogs(true)
                .withStdIn(stdin)
                .exec(callback)
                .awaitCompletion(30, TimeUnit.SECONDS);
        callback.close();
    }

    public static class AttachContainerTestCallback extends AttachContainerResultCallback {
        private StringBuffer log = new StringBuffer();

        @Override
        public void onNext(Frame item) {
            log.append(new String(item.getPayload()));
            super.onNext(item);
        }

        @Override
        public String toString() {
            return log.toString();
        }
    }
}
