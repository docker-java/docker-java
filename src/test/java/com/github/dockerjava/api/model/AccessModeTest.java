package com.github.dockerjava.api.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.dockerjava.api.model.AccessMode.rw;
import static org.junit.Assert.assertEquals;


public class AccessModeTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

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

    @Test
    public void fromIllegalString() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("No enum const");

        AccessMode.valueOf("xx");
    }

}
