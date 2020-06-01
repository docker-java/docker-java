package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.apache.commons.compress.utils.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.notNullValue;

public class ExposedPortsTest {

    @Test
    public void usesToJson() throws Exception {
        ExposedPorts ports = new ExposedPorts(
            new ExposedPort(80),
            new ExposedPort(123, InternetProtocol.UDP),
            new ExposedPort(3868, InternetProtocol.SCTP)
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(ports);
        List<Entry<String, JsonNode>> jsonEntries = getJsonEntries(json);

        String jsonExpected = "{\"80/tcp\":{},\"123/udp\":{},\"3868/sctp\":{}}";
        List<Entry<String, JsonNode>> jsonEntriesExpected = getJsonEntries(jsonExpected);

        assertThat(jsonEntries.toArray(), arrayContainingInAnyOrder(jsonEntriesExpected.toArray()));
    }

    private List<Entry<String, JsonNode>> getJsonEntries(String json) throws Exception {
        JsonNode jsonNode = JSONTestHelper.getMapper().readValue(json, JsonNode.class);
        return Lists.newArrayList(jsonNode.fields());
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

    @Test
    public void usesFromJsonWithDuplicate() throws Exception {
        ExposedPorts ports = new ExposedPorts(
            new ExposedPort(80, InternetProtocol.UDP),
            new ExposedPort(80),
            new ExposedPort(80)
        );

        assertThat(ports, notNullValue());
        assertThat(ports.getExposedPorts(), arrayWithSize(3));

        Map<String, Object> map = ports.toPrimitive();
        assertThat(map, aMapWithSize(2));
    }
}
