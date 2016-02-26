package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.DockerClientBuilder;

@Test(groups = "integration")
public class FrameReaderITest {

    private DockerClient dockerClient;

    private DockerfileFixture dockerfileFixture;

    @BeforeTest
    public void beforeTest() throws Exception {
        dockerClient = DockerClientBuilder.getInstance().build();
        dockerfileFixture = new DockerfileFixture(dockerClient, "frameReaderDockerfile");
        dockerfileFixture.open();
    }

    @AfterTest
    public void deleteDockerContainerImage() throws Exception {
        dockerfileFixture.close();
        dockerClient.close();
    }

    @Test
    public void canCloseFrameReaderAndReadExpectedLines() throws Exception {
        // wait for the container to be successfully executed
        int exitCode = dockerClient.waitContainerCmd(dockerfileFixture.getContainerId())
                .exec(new WaitContainerResultCallback()).awaitStatusCode();
        assertEquals(0, exitCode);

        final List<Frame> loggingFrames = getLoggingFrames();
        final Frame outFrame = new Frame(StreamType.STDOUT, "to stdout\n".getBytes());
        final Frame errFrame = new Frame(StreamType.STDERR, "to stderr\n".getBytes());
        
        assertThat(loggingFrames, containsInAnyOrder(outFrame, errFrame));
        assertThat(loggingFrames, hasSize(2));
    }

    private List<Frame> getLoggingFrames() throws Exception {

        FrameReaderITestCallback collectFramesCallback = new FrameReaderITestCallback();

        dockerClient.logContainerCmd(dockerfileFixture.getContainerId()).withStdOut(true).withStdErr(true)
                .withTailAll()
                // we can't follow stream here as it blocks reading from resulting InputStream infinitely
                // .withFollowStream()
                .exec(collectFramesCallback).awaitCompletion();

        return collectFramesCallback.frames;
    }

    @Test
    public void canLogInOneThreadAndExecuteCommandsInAnother() throws Exception {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Iterator<Frame> frames = getLoggingFrames().iterator();

                    while (frames.hasNext()) {
                        frames.next();
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread.start();

        try (DockerfileFixture busyboxDockerfile = new DockerfileFixture(dockerClient, "busyboxDockerfile")) {
            busyboxDockerfile.open();
        }

        thread.join();

    }

    public static class FrameReaderITestCallback extends LogContainerResultCallback {

        public List<Frame> frames = new ArrayList<Frame>();

        @Override
        public void onNext(Frame item) {
            frames.add(item);
            super.onNext(item);
        }

    }
}
