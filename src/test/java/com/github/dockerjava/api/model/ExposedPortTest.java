package com.github.dockerjava.api.model;

import static com.github.dockerjava.api.model.InternetProtocol.DEFAULT;
import static com.github.dockerjava.api.model.InternetProtocol.TCP;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ExposedPortTest {

    @Test
    public void parsePortAndProtocol() {
        ExposedPort exposedPort = ExposedPort.parse("80/tcp");
        assertEquals(exposedPort, new ExposedPort(80, TCP));
    }

    @Test
    public void parsePortOnly() {
        ExposedPort exposedPort = ExposedPort.parse("80");
        assertEquals(exposedPort, new ExposedPort(80, DEFAULT));
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
        assertEquals(ExposedPort.parse("80/tcp").toString(), "80/tcp");
    }

}
