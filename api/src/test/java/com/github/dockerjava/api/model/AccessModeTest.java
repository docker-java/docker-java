package com.github.dockerjava.api.model;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class AccessModeTest {

    @Test
    public void defaultAccessMode() {
        assertEquals(AccessMode.DEFAULT, AccessMode.rw);
    }

    @Test
    public void stringify() {
        assertEquals(AccessMode.rw.toString(), "rw");
    }

    @Test
    public void fromString() {
        assertEquals(AccessMode.valueOf("rw"), AccessMode.rw);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "No enum const.*")
    public void fromIllegalString() {
        AccessMode.valueOf("xx");
    }

}
