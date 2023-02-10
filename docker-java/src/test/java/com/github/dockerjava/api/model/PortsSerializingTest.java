package com.github.dockerjava.api.model;

import com.github.dockerjava.api.model.Ports.Binding;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PortsSerializingTest {

    private final String jsonWithDoubleBindingForOnePort = "{\"80/tcp\":[{\"HostIp\":\"10.0.0.1\",\"HostPort\":\"80\"},{\"HostIp\":\"10.0.0.2\",\"HostPort\":\"80\"}]}";

    private final String jsonWithNullBindingForOnePort = "{\"80/tcp\":null}";

    @Test
    public void deserializingPortWithMultipleBindings() throws Exception {
        Ports ports = JSONTestHelper.getMapper().readValue(jsonWithDoubleBindingForOnePort, Ports.class);
        Map<ExposedPort, Binding[]> map = ports.getBindings();
        assertEquals(1, map.size());

        Binding[] bindings = map.get(ExposedPort.tcp(80));
        assertEquals(2, bindings.length);
        assertEquals(new Binding("10.0.0.1", "80"), bindings[0]);
        assertEquals(new Binding("10.0.0.2", "80"), bindings[1]);
    }

    @Test
    public void serializingPortWithMultipleBindings() throws Exception {
        Ports ports = new Ports();
        ports.bind(ExposedPort.tcp(80), new Binding("10.0.0.1", "80"));
        ports.bind(ExposedPort.tcp(80), new Binding("10.0.0.2", "80"));
        assertEquals(jsonWithDoubleBindingForOnePort, JSONTestHelper.getMapper().writeValueAsString(ports));
    }

    @Test
    public void serializingEmptyBinding() throws Exception {
        Ports ports = new Ports(ExposedPort.tcp(80), new Binding(null, null));
        assertEquals("{\"80/tcp\":[{\"HostIp\":\"\",\"HostPort\":\"\"}]}", JSONTestHelper.getMapper().writeValueAsString(ports));
    }

    @Test
    public void deserializingPortWithNullBindings() throws Exception {
        Ports ports = JSONTestHelper.getMapper().readValue(jsonWithNullBindingForOnePort, Ports.class);
        Map<ExposedPort, Binding[]> map = ports.getBindings();
        assertEquals(1, map.size());

        assertNull(map.get(ExposedPort.tcp(80)));
    }

    @Test
    public void serializingWithNullBindings() throws Exception {
        Ports ports = new Ports();
        ports.bind(ExposedPort.tcp(80), null);
        assertEquals(jsonWithNullBindingForOnePort, JSONTestHelper.getMapper().writeValueAsString(ports));
    }
}
