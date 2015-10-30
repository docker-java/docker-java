package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class BindTest {

    @Test
    public void parseUsingDefaultAccessMode() {
        Bind bind = Bind.parse("/host:/container");
        Assert.assertEquals(bind.getPath(), "/host");
        Assert.assertEquals(bind.getVolume().getPath(), "/container");
        Assert.assertEquals(bind.getAccessMode(), AccessMode.DEFAULT);
    }

    @Test
    public void parseReadWrite() {
        Bind bind = Bind.parse("/host:/container:rw");
        Assert.assertEquals(bind.getPath(), "/host");
        Assert.assertEquals(bind.getVolume().getPath(), "/container");
        Assert.assertEquals(bind.getAccessMode(), AccessMode.rw);
    }

    @Test
    public void parseReadOnly() {
        Bind bind = Bind.parse("/host:/container:ro");
        Assert.assertEquals(bind.getPath(), "/host");
        Assert.assertEquals(bind.getVolume().getPath(), "/container");
        Assert.assertEquals(bind.getAccessMode(), AccessMode.ro);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Bind.*")
    public void parseInvalidAccessMode() {
        Bind.parse("/host:/container:xx");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Bind 'nonsense'")
    public void parseInvalidInput() {
        Bind.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Bind 'null'")
    public void parseNull() {
        Bind.parse(null);
    }

    @Test
    public void toStringReadOnly() {
        Assert.assertEquals(Bind.parse("/host:/container:ro").toString(), "/host:/container:ro");
    }

    @Test
    public void toStringReadWrite() {
        Assert.assertEquals(Bind.parse("/host:/container:rw").toString(), "/host:/container:rw");
    }

    @Test
    public void toStringDefaultAccessMode() {
        Assert.assertEquals(Bind.parse("/host:/container").toString(), "/host:/container:rw");
    }

}
