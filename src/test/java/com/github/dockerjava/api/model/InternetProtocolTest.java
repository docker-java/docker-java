package com.github.dockerjava.api.model;

import static com.github.dockerjava.api.model.InternetProtocol.TCP;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class InternetProtocolTest {

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

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Protocol.*")
    public void parseInvalidInput() {
        InternetProtocol.parse("xx");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing Protocol 'null'")
    public void parseNull() {
        InternetProtocol.parse(null);
    }

}
