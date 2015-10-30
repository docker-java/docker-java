package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class VolumeTest {
    @Test
    public void getPath() {
        Assert.assertEquals(new Volume("/path").getPath(), "/path");
    }
}
