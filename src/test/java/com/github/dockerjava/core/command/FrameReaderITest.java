package com.github.dockerjava.core.command;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.Iterator;
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
        int exitCode = dockerClient.waitContainerCmd(dockerfileFixture.getContainerId()).exec();
        assertEquals(0, exitCode);

        Iterator<Frame> response = getLoggingFrames().iterator();

        assertEquals(response.next(), new Frame(StreamType.STDOUT, "to stdout\n".getBytes()));
        assertEquals(response.next(), new Frame(StreamType.STDERR, "to stderr\n".getBytes()));
        assertFalse(response.hasNext());

    }

    private List<Frame> getLoggingFrames() throws Exception {

        AbstractDockerClientTest.CollectFramesCallback collectFramesCallback = new AbstractDockerClientTest.CollectFramesCallback();

        dockerClient.logContainerCmd(dockerfileFixture.getContainerId(), collectFramesCallback).withStdOut()
                .withStdErr().withTailAll()
                // we can't follow stream here as it blocks reading from resulting InputStream infinitely
                // .withFollowStream()
                .exec();

        collectFramesCallback.awaitFinish();

        return collectFramesCallback.items;
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
}
