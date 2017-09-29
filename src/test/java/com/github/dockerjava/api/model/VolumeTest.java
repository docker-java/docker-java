package com.github.dockerjava.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VolumeTest {
    @Test
    public void getPath() {
        assertEquals(new Volume("/path").getPath(), "/path");
    }
}
