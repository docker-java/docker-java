package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VolumeBindsTest {
    @Test
    public void t() throws IOException {
        String s = "{\"/data\":\"/some/path\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        VolumeBinds volumeBinds = objectMapper.readValue(s, VolumeBinds.class);
        VolumeBind[] binds = volumeBinds.getBinds();
        assertEquals(binds.length, 1);
        assertEquals(binds[0].getHostPath(), "/some/path");
        assertEquals(binds[0].getContainerPath(), "/data");
    }

    @Test(expectedExceptions = JsonMappingException.class)
    public void t1() throws IOException {
        String s = "{\"/data\": {} }";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue(s, VolumeBinds.class);
    }

}
