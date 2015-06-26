package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Test(expectedExceptions = JsonMappingException.class)
    public void deserializeInvalidCapability() throws Exception {
        objectMapper.readValue("\"nonsense\"", Capability.class);
    }
}
