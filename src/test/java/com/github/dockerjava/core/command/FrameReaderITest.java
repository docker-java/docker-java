package com.github.dockerjava.core.command;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

import java.io.IOException;
import java.io.InputStream;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
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

        InputStream response = getLoggerStream();

        try (FrameReader reader = new FrameReader(response)) {
            assertEquals(reader.readFrame(), new Frame(StreamType.STDOUT, "to stdout\n".getBytes()));
            assertEquals(reader.readFrame(), new Frame(StreamType.STDERR, "to stderr\n".getBytes()));
            assertNull(reader.readFrame());
        }
    }

    private InputStream getLoggerStream() {

        return dockerClient.logContainerCmd(dockerfileFixture.getContainerId()).withStdOut().withStdErr().withTailAll()
        // we can't follow stream here as it blocks reading from resulting InputStream infinitely
        // .withFollowStream()
                .exec();
    }

    @Test
    public void canLogInOneThreadAndExecuteCommandsInAnother() throws Exception {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try (FrameReader reader = new FrameReader(getLoggerStream())) {
                        // noinspection StatementWithEmptyBody
                        while (reader.readFrame() != null) {
                            // nop
                        }
                    }
                } catch (IOException e) {
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
