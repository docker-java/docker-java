package com.github.dockerjava.api.model;

import com.github.dockerjava.api.model.Ports.Binding;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PortBindingTest {

    private static final ExposedPort TCP_8080 = ExposedPort.tcp(8080);

    @Test
    public void fullDefinition() {
        Assert.assertEquals(PortBinding.parse("127.0.0.1:80:8080/tcp"),
                new PortBinding(new Binding("127.0.0.1", 80), TCP_8080));
    }

    @Test
    public void noProtocol() {
        Assert.assertEquals(PortBinding.parse("127.0.0.1:80:8080"), new PortBinding(new Binding("127.0.0.1", 80), TCP_8080));
    }

    @Test
    public void noHostIp() {
        Assert.assertEquals(PortBinding.parse("80:8080/tcp"), new PortBinding(new Binding(80), TCP_8080));
    }

    @Test
    public void portsOnly() {
        Assert.assertEquals(PortBinding.parse("80:8080"), new PortBinding(new Binding(80), TCP_8080));
    }

    @Test
    public void exposedPortOnly() {
        Assert.assertEquals(PortBinding.parse("8080"), new PortBinding(new Binding(), TCP_8080));
    }

    @Test
    public void dynamicHostPort() {
        Assert.assertEquals(PortBinding.parse("127.0.0.1::8080"), new PortBinding(new Binding("127.0.0.1"), TCP_8080));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing PortBinding 'nonsense'")
    public void parseInvalidInput() {
        PortBinding.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing PortBinding 'null'")
    public void parseNull() {
        PortBinding.parse(null);
    }

}
