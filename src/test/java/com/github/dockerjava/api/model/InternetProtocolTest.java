package com.github.dockerjava.api.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.dockerjava.api.model.InternetProtocol.TCP;
import static org.junit.Assert.assertEquals;

public class InternetProtocolTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void defaultProtocol() {
        assertEquals(InternetProtocol.DEFAULT, TCP);
    }

    @Test
    public void stringify() {
        assertEquals(TCP.toString(), "tcp");
    }

    @Test
    public void parseUpperCase() {
        assertEquals(InternetProtocol.parse("TCP"), TCP);
    }

    @Test
    public void parseLowerCase() {
        assertEquals(InternetProtocol.parse("tcp"), TCP);
    }

    @Test
    public void parseInvalidInput() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Protocol 'xx'");

        InternetProtocol.parse("xx");
    }

    @Test
    public void parseNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing Protocol 'null'");

        InternetProtocol.parse(null);
    }

}
