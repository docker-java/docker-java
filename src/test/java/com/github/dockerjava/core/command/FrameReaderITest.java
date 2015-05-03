package com.github.dockerjava.core.command;


import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

@Test(groups = "integration")
public class FrameReaderITest extends AbstractDockerClientTest {

    private DockerfileFixture dockerfileFixture;

    @BeforeTest
    @Override
    public void beforeTest() {
        super.beforeTest();
        dockerfileFixture = new DockerfileFixture(dockerClient, "frameReaderDockerfile");
    }

    @BeforeMethod
    public void createAndStartDockerContainer() throws Exception {
        dockerfileFixture.open();
    }

    @AfterMethod
    public void deleteDockerContainerImage() throws Exception {
        dockerfileFixture.close();
    }

    @AfterTest
    @Override
    public void afterTest() {
        super.afterTest();
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