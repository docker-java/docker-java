package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.notNullValue;

public class ImageHistoryTest {
    @Test
    public void serderDocs1() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, ImageHistory.class);

        final List<ImageHistory> history = testRoundTrip(RemoteApiVersion.VERSION_1_24,
                "/imageHistory/docs1.json",
                type
        );

        assertThat(history, notNullValue());

        ImageHistory firstEntry = history.get(0);
        assertThat(firstEntry.getId(), is("3db9c44f45209632d6050b35958829c3a2aa256d81b9a7be45b362ff85c54710"));
        assertThat(firstEntry.getCreated(), is(1398108230L));
        assertThat(firstEntry.getCreatedBy(),
                is("/bin/sh -c #(nop) ADD file:eb15dbd63394e063b805a3c32ca7bf0266ef64676d5a6fab4801f2e81e2a5148 in /"));
        List<String> firstEntryTags = firstEntry.getTags();
        assertThat(firstEntryTags, notNullValue());
        assertThat(firstEntryTags, hasSize(2));
        assertThat(firstEntryTags.get(0), is("ubuntu:lucid"));
        assertThat(firstEntryTags.get(1), is("ubuntu:10.04"));
        assertThat(firstEntry.getSize(), is(182964289L));
        assertThat(firstEntry.getComment(), isEmptyString());

        List<String> newFirstEntryTags = new ArrayList<>();
        newFirstEntryTags.add("ubuntu:lucid");
        newFirstEntryTags.add("ubuntu:10.04");
        ImageHistory newFirstEntry = new ImageHistory()
                .withId("3db9c44f45209632d6050b35958829c3a2aa256d81b9a7be45b362ff85c54710")
                .withCreated(1398108230L)
                .withCreatedBy("/bin/sh -c #(nop) ADD file:eb15dbd63394e063b805a3c32ca7bf0266ef64676d5a6fab4801f2e81e2a5148 in /")
                .withTags(newFirstEntryTags)
                .withSize(182964289L)
                .withComment("");
        assertThat(newFirstEntry, is(firstEntry));

        ImageHistory secondEntry = history.get(1);
        assertThat(secondEntry.getId(), is("6cfa4d1f33fb861d4d114f43b25abd0ac737509268065cdfd69d544a59c85ab8"));
        assertThat(secondEntry.getCreated(), is(1398108222L));
        assertThat(secondEntry.getCreatedBy(), is("/bin/sh -c #(nop) MAINTAINER Tianon Gravi <admwiggin@gmail.com> - "
                + "mkimage-debootstrap.sh -i iproute,iputils-ping,ubuntu-minimal -t lucid.tar.xz lucid "
                + "http://archive.ubuntu.com/ubuntu/"));
        assertThat(secondEntry.getTags(), nullValue());
        assertThat(secondEntry.getSize(), is(0L));
        assertThat(secondEntry.getComment(), isEmptyString());

        ImageHistory newSecondEntry = new ImageHistory()
                .withId("6cfa4d1f33fb861d4d114f43b25abd0ac737509268065cdfd69d544a59c85ab8")
                .withCreated(1398108222L)
                .withCreatedBy("/bin/sh -c #(nop) MAINTAINER Tianon Gravi <admwiggin@gmail.com> - "
                        + "mkimage-debootstrap.sh -i iproute,iputils-ping,ubuntu-minimal -t lucid.tar.xz lucid "
                        + "http://archive.ubuntu.com/ubuntu/")
                .withSize(0L)
                .withComment("");
        assertThat(newSecondEntry, is(secondEntry));

        ImageHistory thirdEntry = history.get(2);
        assertThat(thirdEntry.getId(), is("511136ea3c5a64f264b78b5433614aec563103b4d4702f3ba7d4d2698e22c158"));
        assertThat(thirdEntry.getCreated(), is(1371157430L));
        assertThat(thirdEntry.getCreatedBy(), isEmptyString());
        List<String> thirdEntryTags = thirdEntry.getTags();
        assertThat(thirdEntryTags, notNullValue());
        assertThat(thirdEntryTags, hasSize(2));
        assertThat(thirdEntryTags.get(0), is("scratch12:latest"));
        assertThat(thirdEntryTags.get(1), is("scratch:latest"));
        assertThat(thirdEntry.getSize(), is(0L));
        assertThat(thirdEntry.getComment(), is("Imported from -"));

        List<String> newThirdEntryTags = new ArrayList<>();
        newThirdEntryTags.add("scratch12:latest");
        newThirdEntryTags.add("scratch:latest");
        ImageHistory newThirdEntry = new ImageHistory()
                .withId("511136ea3c5a64f264b78b5433614aec563103b4d4702f3ba7d4d2698e22c158")
                .withCreated(1371157430L)
                .withCreatedBy("")
                .withTags(newThirdEntryTags)
                .withSize(0L)
                .withComment("Imported from -");
        assertThat(newThirdEntry, is(thirdEntry));
    }
}
