package com.github.dockerjava.core.command;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.DockerClientBuilder;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNull;

@Test(groups = "integration")
public class FrameReaderITest  {

    private DockerClient dockerClient;
    private DockerfileFixture dockerfileFixture;

    @BeforeTest
    public void beforeTest() {
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

        try (FrameReader reader = new FrameReader(getLoggerStream())) {
            assertEquals(reader.readFrame(), new Frame(StreamType.STDOUT, String.format("to stdout%n").getBytes()));
            assertEquals(reader.readFrame(), new Frame(StreamType.STDERR, String.format("to stderr%n").getBytes()));
            assertNull(reader.readFrame());
        }
    }

    private InputStream getLoggerStream() {
        return dockerClient
                .logContainerCmd(dockerfileFixture.getContainerId())
                .withStdOut()
                .withStdErr()
                .withTailAll()
                .withTail(10)
                .withFollowStream()
                .exec();
    }

    @Test
    public void canLogInOneThreadAndExecuteCommandsInAnother() throws Exception {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try (FrameReader reader = new FrameReader(getLoggerStream())) {
                        //noinspection StatementWithEmptyBody
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