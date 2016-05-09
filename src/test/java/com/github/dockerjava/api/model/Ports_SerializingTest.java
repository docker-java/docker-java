package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Ports.Binding;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;

public class Ports_SerializingTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String jsonWithDoubleBindingForOnePort = "{\"80/tcp\":[{\"HostIp\":\"10.0.0.1\",\"HostPort\":\"80\"},{\"HostIp\":\"10.0.0.2\",\"HostPort\":\"80\"}]}";

    private final String jsonWithNullBindingForOnePort = "{\"80/tcp\":null}";

    @Test
    public void deserializingPortWithMultipleBindings() throws Exception {
        Ports ports = objectMapper.readValue(jsonWithDoubleBindingForOnePort, Ports.class);
        Map<ExposedPort, Binding[]> map = ports.getBindings();
        assertEquals(map.size(), 1);

        Binding[] bindings = map.get(ExposedPort.tcp(80));
        assertEquals(bindings.length, 2);
        assertEquals(bindings[0], new Binding("10.0.0.1", "80"));
        assertEquals(bindings[1], new Binding("10.0.0.2", "80"));
    }

    @Test
    public void serializingPortWithMultipleBindings() throws Exception {
        Ports ports = new Ports();
        ports.bind(ExposedPort.tcp(80), new Binding("10.0.0.1", "80"));
        ports.bind(ExposedPort.tcp(80), new Binding("10.0.0.2", "80"));
        assertEquals(objectMapper.writeValueAsString(ports), jsonWithDoubleBindingForOnePort);
    }

    @Test
    public void serializingEmptyBinding() throws Exception {
        Ports ports = new Ports(ExposedPort.tcp(80), new Binding(null, null));
        assertEquals(objectMapper.writeValueAsString(ports), "{\"80/tcp\":[{\"HostIp\":\"\",\"HostPort\":\"\"}]}");
    }

    @Test
    public void deserializingPortWithNullBindings() throws Exception {
        Ports ports = objectMapper.readValue(jsonWithNullBindingForOnePort, Ports.class);
        Map<ExposedPort, Binding[]> map = ports.getBindings();
        assertEquals(map.size(), 1);

        assertEquals(map.get(ExposedPort.tcp(80)), null);
    }

    @Test
    public void serializingWithNullBindings() throws Exception {
        Ports ports = new Ports();
        ports.bind(ExposedPort.tcp(80), null);
        assertEquals(objectMapper.writeValueAsString(ports), jsonWithNullBindingForOnePort);
    }
}
