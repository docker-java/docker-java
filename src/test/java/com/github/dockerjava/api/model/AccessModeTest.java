package com.github.dockerjava.api.model;

import static com.github.dockerjava.api.model.AccessMode.rw;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class AccessModeTest {

    @Test
    public void defaultAccessMode() {
        assertEquals(AccessMode.DEFAULT, rw);
    }

    @Test
    public void stringify() {
        assertEquals(AccessMode.rw.toString(), "rw");
    }

    @Test
    public void fromString() {
        assertEquals(AccessMode.valueOf("rw"), rw);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "No enum const.*")
    public void fromIllegalString() {
        AccessMode.valueOf("xx");
    }

}
