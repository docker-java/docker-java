package com.github.dockerjava.core;

import org.apache.commons.lang.SerializationUtils;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Kanstantsin Shautsou
 */
public class RemoteApiVersionTest {
    @Test
    public void testSerial() {
        SerializationUtils.serialize(RemoteApiVersion.unknown());
        final RemoteApiVersion remoteApiVersion = RemoteApiVersion.create(1, 20);

        final byte[] serialized = SerializationUtils.serialize(remoteApiVersion);
        RemoteApiVersion deserialized = (RemoteApiVersion) SerializationUtils.deserialize(serialized);

        assertThat("Deserialized object mush match source object", deserialized, equalTo(remoteApiVersion));
    }
}
