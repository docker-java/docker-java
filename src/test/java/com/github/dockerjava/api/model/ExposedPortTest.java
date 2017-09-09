package com.github.dockerjava.api.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.dockerjava.api.model.InternetProtocol.DEFAULT;
import static com.github.dockerjava.api.model.InternetProtocol.TCP;
import static org.junit.Assert.assertEquals;

public class ExposedPortTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

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

    @Test
    public void parseInvalidInput() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing ExposedPort 'nonsense'");

        ExposedPort.parse("nonsense");
    }

    @Test
    public void parseNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing ExposedPort 'null'");

        ExposedPort.parse(null);
    }

    @Test
    public void stringify() {
        assertEquals(ExposedPort.parse("80/tcp").toString(), "80/tcp");
    }

}
