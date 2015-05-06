package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.EventStreamItem;
import com.github.dockerjava.api.model.PullEventStreamItem;
import com.github.dockerjava.core.DockerClientBuilder;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.testng.AssertJUnit.assertNull;


@Test(groups = "integration")
public class EventStreamReaderITest  {

    private DockerClient dockerClient;

    @BeforeTest
    public void setUp() throws Exception {
        dockerClient = DockerClientBuilder.getInstance().build();
    }

    @AfterTest
    public void tearDown() throws Exception {
        dockerClient.close();
    }

    @Test
    public void pullCanBeStreamed() throws Exception {

        try (EventStreamReader<PullEventStreamItem> reader = new EventStreamReader<>(
                dockerClient.pullImageCmd("busybox:latest").exec(),
                PullEventStreamItem.class)
        ) {;
            assertThat(reader.readItem(),
                    allOf(
                            hasProperty("status", equalTo("Pulling repository busybox")),
                            hasProperty("progress", nullValue()),
                            hasProperty("progressDetail", nullValue())
                    )
                );
            assertNull(reader.readItem());
        }
    }

    @Test
    public void buildCanBeStreamed() throws Exception {

        try (EventStreamReader<EventStreamItem> reader = new EventStreamReader<>(
                dockerClient.buildImageCmd(new File("src/test/resources/eventStreamReaderDockerfile")).exec(),
                EventStreamItem.class)
        ) {
            assertThat(reader.readItem(),
                    allOf(
                            hasProperty("stream", equalTo("Step 0 : FROM busybox:latest\n")),
                            hasProperty("error", nullValue()),
                            hasProperty("errorDetail", nullValue())
                    )
            );
            assertNull(reader.readItem());

        }
    }
}