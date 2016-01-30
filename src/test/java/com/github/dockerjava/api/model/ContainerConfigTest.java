package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class ContainerConfigTest {

    @Test
    public void missingExposedPortsReturnEmptyArray() throws IOException {
        String s = "{}";
        ObjectMapper objectMapper = new ObjectMapper();
        ContainerConfig config = objectMapper.readValue(s, ContainerConfig.class);
        assertEquals(0, config.getExposedPorts().length);
    }

    @Test
    public void nullExposedPortsReturnEmptyArray() throws IOException {
        String s = "{\"ExposedPorts\": null}";
        ObjectMapper objectMapper = new ObjectMapper();
        ContainerConfig config = objectMapper.readValue(s, ContainerConfig.class);
        assertEquals(0, config.getExposedPorts().length);
    }

    @Test
    public void exposedPortsReturnArray() throws IOException {
        String s = "{\"ExposedPorts\": {\"22/tcp\": {}, \"80/tcp\": {}}}";
        ObjectMapper objectMapper = new ObjectMapper();
        ContainerConfig config = objectMapper.readValue(s, ContainerConfig.class);
        ExposedPort[] ports = config.getExposedPorts();
        assertEquals(2, ports.length);
        ExposedPort port22tcp = ports[0];
        assertEquals(22, port22tcp.getPort());
        assertEquals(InternetProtocol.TCP, port22tcp.getProtocol());
        ExposedPort port80tcp = ports[1];
        assertEquals(80, port80tcp.getPort());
        assertEquals(InternetProtocol.TCP, port80tcp.getProtocol());
    }

}
