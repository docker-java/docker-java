package com.github.dockerjava.api.model;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class InternetProtocolTest {

    @Test
    public void defaultProtocol() {
        assertEquals(InternetProtocol.DEFAULT, InternetProtocol.TCP);
    }

    @Test
    public void stringify() {
        assertEquals(InternetProtocol.TCP.toString(), "tcp");
    }

    @Test
    public void parseUpperCase() {
        assertEquals(InternetProtocol.parse("TCP"), InternetProtocol.TCP);
    }

    @Test
    public void parseLowerCase() {
        assertEquals(InternetProtocol.parse("tcp"), InternetProtocol.TCP);
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Protocol.*")
    public void parseInvalidInput() {
        InternetProtocol.parse("xx");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Protocol 'null'")
    public void parseNull() {
        InternetProtocol.parse(null);
    }

}
