package com.github.dockerjava.api.model;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class VolumeTest {
    @Test
    public void getPath() {
        assertEquals(new Volume("/path").getPath(), "/path");
    }
}
