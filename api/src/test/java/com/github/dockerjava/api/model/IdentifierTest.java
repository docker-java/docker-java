package com.github.dockerjava.api.model;

import junit.framework.TestCase;
import org.testng.Assert;

public class IdentifierTest extends TestCase {

    public void testFromCompoundString() throws Exception {

        Identifier i1 = Identifier.fromCompoundString("10.0.0.1/jim");
        Identifier i2 = Identifier.fromCompoundString("10.0.0.1/jim:123");
        Identifier i3 = Identifier.fromCompoundString("10.0.0.1:123/jim:124");
        Identifier i3A = Identifier.fromCompoundString("10.0.0.1:123/jim:latest");

        Assert.assertTrue(!i1.tag.isPresent());
        Assert.assertEquals(i1.repository.name, "10.0.0.1/jim");

        Assert.assertTrue(i2.tag.isPresent());
        Assert.assertEquals(i2.tag.get(), "123");
        Assert.assertEquals(i2.repository.name, "10.0.0.1/jim");

        Assert.assertTrue(i3.tag.isPresent());
        Assert.assertEquals(i3.tag.get(), "124");
        Assert.assertEquals(i3.repository.name, "10.0.0.1:123/jim");
        Assert.assertEquals(i3.repository.getURL().getPort(), 123);
        Assert.assertEquals(i3A.tag.get(), "latest");

        Identifier i4 = Identifier.fromCompoundString("centos:latest");
        Assert.assertTrue(i4.tag.isPresent());
        Assert.assertEquals(i4.tag.get(), "latest");

        Identifier i5 = Identifier.fromCompoundString("busybox");
        Assert.assertTrue(!i5.tag.isPresent());

        Identifier i6 = Identifier.fromCompoundString("10.0.0.1:5000/my-test-image:1234");
        Assert.assertEquals(i6.repository.getPath(), "my-test-image");
    }
}