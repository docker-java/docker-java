package com.github.dockerjava.core.command;


import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;

@Test(groups = "integration")
public class FrameReaderITest extends AbstractDockerClientTest {

    private DockerfileFixture dockerfileFixture;

    @BeforeMethod
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

    @AfterMethod
    @Override
    public void afterTest() {
        super.afterTest();
    }

    @Test
    public void canCloseFrameReaderAndReadExpectedLinens() throws Exception {

        InputStream log = dockerClient
                .logContainerCmd(dockerfileFixture.getContainerId())
                .withStdOut()
                .withStdErr()
                .withFollowStream()
                .withTailAll()
                .exec();

        try (FrameReader reader = new FrameReader(log)) {
            assertEquals(reader.readFrame(), new Frame(StreamType.STDOUT, String.format("to stdout%n").getBytes()));
            assertEquals(reader.readFrame(), new Frame(StreamType.STDERR, String.format("to stderr%n").getBytes()));
            assertNull(reader.readFrame());
        }
    }
}