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
        assertEquals(new PortBinding(Binding.bindIpAndPort("127.0.0.1", 80), TCP_8080),
            PortBinding.parse("127.0.0.1:80:8080/tcp"));
    }

    @Test
    public void noProtocol() {
        assertEquals(new PortBinding(Binding.bindIpAndPort("127.0.0.1", 80), TCP_8080),
            PortBinding.parse("127.0.0.1:80:8080"));
    }

    @Test
    public void noHostIp() {
        assertEquals(new PortBinding(Binding.bindPort(80), TCP_8080), PortBinding.parse("80:8080/tcp"));
    }

    @Test
    public void portsOnly() {
        assertEquals(new PortBinding(Binding.bindPort(80), TCP_8080), PortBinding.parse("80:8080"));
    }

    @Test
    public void exposedPortOnly() {
        assertEquals(new PortBinding(Binding.empty(), TCP_8080), PortBinding.parse("8080"));
    }

    @Test
    public void dynamicPort() {
        assertEquals(new PortBinding(Binding.empty(), TCP_8080), PortBinding.parse(":8080/tcp"));
    }

    @Test
    public void dynamicHostPort() {
        assertEquals(new PortBinding(Binding.bindIp("127.0.0.1"), TCP_8080), PortBinding.parse("127.0.0.1::8080"));
    }

    @Test
    public void allIfacesdynamicPort() {
        assertEquals(new PortBinding(Binding.empty(), TCP_8080), PortBinding.parse("0.0.0.0:0:8080"));
    }

    @Test
    public void allIfacesPortOnly() {
        assertEquals(new PortBinding(Binding.parse("80"), TCP_8080), PortBinding.parse("0.0.0.0:80:8080"));
    }

    @Test
    public void macOSdynamicPort() {
        assertEquals(new PortBinding(Binding.parse("127.0.0.1"), TCP_8080), PortBinding.parse("127.0.0.1:0:8080"));
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
