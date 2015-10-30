package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class InternetProtocolTest {

    @Test
    public void defaultProtocol() {
        Assert.assertEquals(InternetProtocol.DEFAULT, InternetProtocol.TCP);
    }

    @Test
    public void stringify() {
        Assert.assertEquals(InternetProtocol.TCP.toString(), "tcp");
    }

    @Test
    public void parseUpperCase() {
        Assert.assertEquals(InternetProtocol.parse("TCP"), InternetProtocol.TCP);
    }

    @Test
    public void parseLowerCase() {
        Assert.assertEquals(InternetProtocol.parse("tcp"), InternetProtocol.TCP);
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
