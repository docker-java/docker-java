package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.junit.Test;

public class VolumeTest {
    @Test
    public void getPath() {
        assertEquals(new Volume("/path").getPath(), "/path");
    }
}
