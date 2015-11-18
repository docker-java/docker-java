package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class LinkTest {

    @Test
    public void parse() {
        Link link = Link.parse("name:alias");
        assertEquals(link.getName(), "name");
        assertEquals(link.getAlias(), "alias");
    }

    @Test
    public void parseWithContainerNames() {
        Link link = Link.parse("/name:/conatiner/alias");
        assertEquals(link.getName(), "name");
        assertEquals(link.getAlias(), "alias");
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
        assertEquals(Link.parse("name:alias").toString(), "name:alias");
    }

}
