package com.github.dockerjava.api.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class LinkTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void parse() {
        Link link = Link.parse("name:alias");
        assertEquals("name", link.getName());
        assertEquals("alias", link.getAlias());
    }

    @Test
    public void parseWithContainerNames() {
        Link link = Link.parse("/name:/conatiner/alias");
        assertEquals("name", link.getName());
        assertEquals("alias", link.getAlias());
    }

    @Test
    public void parseInvalidInput() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Link 'nonsense'");

        Link.parse("nonsense");
    }

    @Test
    public void parseNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Link 'null'");

        Link.parse(null);
    }

    @Test
    public void stringify() {
        assertEquals("name:alias", Link.parse("name:alias").toString());
    }

}
