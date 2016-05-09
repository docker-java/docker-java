package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.github.dockerjava.api.model.Ports.Binding;

public class PortBindingTest {

    private static final ExposedPort TCP_8080 = ExposedPort.tcp(8080);

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

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing PortBinding 'nonsense'", enabled = false)
    public void parseInvalidInput() {
        PortBinding.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing PortBinding 'null'")
    public void parseNull() {
        PortBinding.parse(null);
    }

}
