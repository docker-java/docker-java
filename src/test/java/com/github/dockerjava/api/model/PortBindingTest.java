package com.github.dockerjava.api.model;

import com.github.dockerjava.api.model.Ports.Binding;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PortBindingTest {

    private static final ExposedPort TCP_8080 = ExposedPort.tcp(8080);

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void fullDefinition() {
        assertEquals(PortBinding.parse("127.0.0.1:80:8080/tcp"),
                new PortBinding(Binding.bindIpAndPort("127.0.0.1", 80), TCP_8080));
    }

    @Test
    public void noProtocol() {
        assertEquals(PortBinding.parse("127.0.0.1:80:8080"), new PortBinding(Binding.bindIpAndPort("127.0.0.1", 80), TCP_8080));
    }

    @Test
    public void noHostIp() {
        assertEquals(PortBinding.parse("80:8080/tcp"), new PortBinding(Binding.bindPort(80), TCP_8080));
    }

    @Test
    public void portsOnly() {
        assertEquals(PortBinding.parse("80:8080"), new PortBinding(Binding.bindPort(80), TCP_8080));
    }

    @Test
    public void exposedPortOnly() {
        assertEquals(PortBinding.parse("8080"), new PortBinding(Binding.empty(), TCP_8080));
    }

    @Test
    public void dynamicHostPort() {
        assertEquals(PortBinding.parse("127.0.0.1::8080"), new PortBinding(Binding.bindIp("127.0.0.1"), TCP_8080));
    }

    @Test
    @Ignore
    public void parseInvalidInput() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing PortBinding 'nonsense'");

        PortBinding.parse("nonsense");
    }

    @Test
    public void parseNull() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing PortBinding 'null'");

        PortBinding.parse(null);
    }

}
