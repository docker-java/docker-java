package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ExposedPortTest {

    @Test
    public void parsePortAndProtocol() {
        ExposedPort exposedPort = ExposedPort.parse("80/tcp");
        Assert.assertEquals(exposedPort, new ExposedPort(80, InternetProtocol.TCP));
    }

    @Test
    public void parsePortOnly() {
        ExposedPort exposedPort = ExposedPort.parse("80");
        Assert.assertEquals(exposedPort, new ExposedPort(80, InternetProtocol.DEFAULT));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing ExposedPort 'nonsense'")
    public void parseInvalidInput() {
        ExposedPort.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing ExposedPort 'null'")
    public void parseNull() {
        ExposedPort.parse(null);
    }

    @Test
    public void stringify() {
        Assert.assertEquals(ExposedPort.parse("80/tcp").toString(), "80/tcp");
    }

}
