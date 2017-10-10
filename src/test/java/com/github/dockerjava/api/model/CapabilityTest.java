package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CapabilityTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializeCapability() throws Exception {
        String json = objectMapper.writeValueAsString(Capability.ALL);
        assertEquals(json, "\"ALL\"");
    }

    @Test
    public void deserializeCapability() throws Exception {
        Capability capability = objectMapper.readValue("\"ALL\"", Capability.class);
        assertEquals(capability, Capability.ALL);
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeInvalidCapability() throws Exception {
        objectMapper.readValue("\"nonsense\"", Capability.class);
    }
}
