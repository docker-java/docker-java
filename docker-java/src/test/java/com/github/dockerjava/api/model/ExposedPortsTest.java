package com.github.dockerjava.api.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ExposedPortsTest {

    @Test
    public void usesToJson() throws Exception {
        ExposedPorts ports = new ExposedPorts(
                new ExposedPort(80),
                new ExposedPort(123, InternetProtocol.UDP),
                new ExposedPort(3868, InternetProtocol.SCTP)
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(ports);
        Map<String, Object> jsonObject = JSONTestHelper.getMapper().readValue(json, new TypeReference<Map<String, Object>>() {});
        String[] jsonObjectKeys = jsonObject.keySet().toArray(new String[0]);

        String expectedJson = "{\"80/tcp\":{},\"123/udp\":{},\"3868/sctp\":{}}";
        Map<String, Object> expectedJsonObject = JSONTestHelper.getMapper().readValue(expectedJson, new TypeReference<Map<String, Object>>() {});
        String[] expectedJsonObjectKeys = expectedJsonObject.keySet().toArray(new String[0]);

        assertThat(jsonObjectKeys, arrayContainingInAnyOrder(expectedJsonObjectKeys));
    }

    @Test
    public void usesFromJson() throws Exception {
        ExposedPorts ports = JSONTestHelper.getMapper().readValue("{\"80/tcp\":{},\"123/udp\":{},\"3868/sctp\":{}}", ExposedPorts.class);

        assertThat(ports, notNullValue());
        assertThat(ports.getExposedPorts(), arrayContainingInAnyOrder(
                new ExposedPort(80),
                new ExposedPort(123, InternetProtocol.UDP),
                new ExposedPort(3868, InternetProtocol.SCTP)
        ));
    }
}
