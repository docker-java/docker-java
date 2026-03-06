package com.github.dockerjava.api.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.test.serdes.JSONSamples;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ImageHistoryTest {

    @Test
    public void serderJson() throws IOException {
        final List<ImageHistory> history = JSONTestHelper.getMapper().readValue(
                JSONSamples.getSampleContent(VERSION_1_22, "images/history/history.json"),
                new TypeReference<List<ImageHistory>>() {
                }
        );

        assertThat(history, notNullValue());
        assertThat(history, hasSize(3));

        final ImageHistory first = history.get(0);
        assertThat(first.getId(), is("3db9c44f45209632d6050b35958829c3a2aa256d81b9a7be45b362ff85c54710"));
        assertThat(first.getCreated(), is(1398108230L));
        assertThat(first.getCreatedBy(), is("/bin/sh -c #(nop) ADD file:eb15dbd63394e063b805a3c32ca7bf0266ef64676d5a6fab4801f2e81e2a5148 in /"));
        assertThat(first.getTags(), hasSize(2));
        assertThat(first.getTags(), contains("ubuntu:lucid", "ubuntu:10.04"));
        assertThat(first.getSize(), is(182964289L));
        assertThat(first.getComment(), is(""));

        final ImageHistory second = history.get(1);
        assertThat(second.getId(), is("6cfa4d1f33fb861d4d114f43b25abd0ac737509268065cdfd69d544a59c85ab8"));
        assertThat(second.getCreated(), is(1398108222L));
        assertThat(second.getTags(), empty());
        assertThat(second.getSize(), is(0L));

        final ImageHistory third = history.get(2);
        assertThat(third.getId(), is("511136ea3c5a64f264b78b5433614aec563103b4d4702f3ba7d4d2698e22c158"));
        assertThat(third.getCreated(), is(1371157430L));
        assertThat(third.getCreatedBy(), is(""));
        assertThat(third.getTags(), contains("scratch12:latest", "scratch:latest"));
        assertThat(third.getSize(), is(0L));
        assertThat(third.getComment(), is("Imported from -"));

        // Test round-trip serialization
        final String serialized = JSONTestHelper.getMapper().writeValueAsString(history);
        final List<ImageHistory> deserialized = JSONTestHelper.getMapper().readValue(
                serialized,
                new TypeReference<List<ImageHistory>>() {
                }
        );
        assertThat(deserialized, hasSize(3));
        assertThat(deserialized.get(0).getId(), is(first.getId()));
        assertThat(deserialized.get(0).getCreated(), is(first.getCreated()));
        assertThat(deserialized.get(0).getCreatedBy(), is(first.getCreatedBy()));
        assertThat(deserialized.get(0).getTags(), is(first.getTags()));
        assertThat(deserialized.get(0).getSize(), is(first.getSize()));
        assertThat(deserialized.get(0).getComment(), is(first.getComment()));
    }

    @Test
    public void builderPattern() {
        final ImageHistory history = new ImageHistory()
                .withId("abc123")
                .withCreated(1234567890L)
                .withCreatedBy("/bin/sh -c echo hello")
                .withTags(Arrays.asList("myimage:latest"))
                .withSize(1024L)
                .withComment("test comment");

        assertThat(history.getId(), is("abc123"));
        assertThat(history.getCreated(), is(1234567890L));
        assertThat(history.getCreatedBy(), is("/bin/sh -c echo hello"));
        assertThat(history.getTags(), contains("myimage:latest"));
        assertThat(history.getSize(), is(1024L));
        assertThat(history.getComment(), is("test comment"));
    }
}
