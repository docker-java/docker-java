package com.github.dockerjava.api.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VolumeTest {
    @Test
    public void getPath() {
        assertEquals("/path", new Volume("/path").getPath());
    }
}
