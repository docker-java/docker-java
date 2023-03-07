package com.github.dockerjava.api.model;


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IdentifierTest {

    @Test
    public void testFromCompoundString() throws Exception {

        Identifier i1 = Identifier.fromCompoundString("10.0.0.1/jim");
        Identifier i2 = Identifier.fromCompoundString("10.0.0.1/jim:123");
        Identifier i3 = Identifier.fromCompoundString("10.0.0.1:123/jim:124");
        Identifier i3A = Identifier.fromCompoundString("10.0.0.1:123/jim:latest");

        assertTrue(!i1.tag.isPresent());
        assertEquals("10.0.0.1/jim", i1.repository.name);

        assertTrue(i2.tag.isPresent());
        assertEquals("123", i2.tag.get());
        assertEquals("10.0.0.1/jim", i2.repository.name);

        assertTrue(i3.tag.isPresent());
        assertEquals("124", i3.tag.get());
        assertEquals("10.0.0.1:123/jim", i3.repository.name);
        assertEquals(123, i3.repository.getURL().getPort());
        assertEquals("latest", i3A.tag.get());

        Identifier i4 = Identifier.fromCompoundString("centos:latest");
        assertTrue(i4.tag.isPresent());
        assertEquals("latest", i4.tag.get());

        Identifier i5 = Identifier.fromCompoundString("busybox");
        assertTrue(!i5.tag.isPresent());

        Identifier i6 = Identifier.fromCompoundString("10.0.0.1:5000/my-test-image:1234");
        assertEquals("my-test-image", i6.repository.getPath());
    }
}
