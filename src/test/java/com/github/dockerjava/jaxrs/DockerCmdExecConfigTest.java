package com.github.dockerjava.jaxrs;

import org.apache.commons.lang.SerializationUtils;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Kanstantsin Shautsou
 */
public class DockerCmdExecConfigTest {
    @Test
    public void serializableTest() {
        final DockerCmdExecConfig dockerCmdExecConfig = DockerCmdExecConfig.create()
                .withConnectTimeout(10)
                .withMaxPerRouteConnections(20)
                .withMaxTotalConnections(100)
                .withReadTimeout(100);

        final byte[] serialized = SerializationUtils.serialize(dockerCmdExecConfig);
        final DockerCmdExecConfig deserialized = (DockerCmdExecConfig) SerializationUtils.deserialize(serialized);

        assertThat("Deserialized object mush match source object", deserialized, equalTo(dockerCmdExecConfig));
    }
}
