package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AccessModeTest {

    @Test
    public void defaultAccessMode() {
        Assert.assertEquals(AccessMode.DEFAULT, AccessMode.rw);
    }

    @Test
    public void stringify() {
        Assert.assertEquals(AccessMode.rw.toString(), "rw");
    }

    @Test
    public void fromString() {
        Assert.assertEquals(AccessMode.valueOf("rw"), AccessMode.rw);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "No enum const.*")
    public void fromIllegalString() {
        AccessMode.valueOf("xx");
    }

}
