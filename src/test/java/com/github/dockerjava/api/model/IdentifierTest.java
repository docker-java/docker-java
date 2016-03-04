package com.github.dockerjava.api.model;


import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

public class IdentifierTest {

    @Test
    public void testFromCompoundString() throws Exception {

        Identifier i1 = Identifier.fromCompoundString("10.0.0.1/jim");
        Identifier i2 = Identifier.fromCompoundString("10.0.0.1/jim:123");
        Identifier i3 = Identifier.fromCompoundString("10.0.0.1:123/jim:124");
        Identifier i3A = Identifier.fromCompoundString("10.0.0.1:123/jim:latest");

        assertTrue(!i1.tag.isPresent());
        assertEquals(i1.repository.name, "10.0.0.1/jim");

        assertTrue(i2.tag.isPresent());
        assertEquals(i2.tag.get(), "123");
        assertEquals(i2.repository.name, "10.0.0.1/jim");

        assertTrue(i3.tag.isPresent());
        assertEquals(i3.tag.get(), "124");
        assertEquals(i3.repository.name, "10.0.0.1:123/jim");
        assertEquals(i3.repository.getURL().getPort(), 123);
        assertEquals(i3A.tag.get(), "latest");

        Identifier i4 = Identifier.fromCompoundString("centos:latest");
        assertTrue(i4.tag.isPresent());
        assertEquals(i4.tag.get(), "latest");

        Identifier i5 = Identifier.fromCompoundString("busybox");
        assertTrue(!i5.tag.isPresent());

        Identifier i6 = Identifier.fromCompoundString("10.0.0.1:5000/my-test-image:1234");
        assertEquals(i6.repository.getPath(), "my-test-image");
    }
}
