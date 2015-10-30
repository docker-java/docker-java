package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class LinkTest {

    @Test
    public void parse() {
        Link link = Link.parse("name:alias");
        Assert.assertEquals(link.getName(), "name");
        Assert.assertEquals(link.getAlias(), "alias");
    }

    @Test
    public void parseWithContainerNames() {
        Link link = Link.parse("/name:/conatiner/alias");
        Assert.assertEquals(link.getName(), "name");
        Assert.assertEquals(link.getAlias(), "alias");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Link 'nonsense'")
    public void parseInvalidInput() {
        Link.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Link 'null'")
    public void parseNull() {
        Link.parse(null);
    }

    @Test
    public void stringify() {
        Assert.assertEquals(Link.parse("name:alias").toString(), "name:alias");
    }

}
