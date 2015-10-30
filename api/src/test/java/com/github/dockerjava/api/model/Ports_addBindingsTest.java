package com.github.dockerjava.api.model;

import com.github.dockerjava.api.model.Ports.Binding;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * As there may be several {@link Binding}s per {@link ExposedPort}, it makes a difference if you add
 * {@link PortBinding}s for the same or different {@link ExposedPort}s to {@link Ports}. This test verifies that the Map
 * in {@link Ports} is populated correctly in both cases.
 */
public class Ports_addBindingsTest {
    private static final ExposedPort TCP_80 = ExposedPort.tcp(80);

    private static final ExposedPort TCP_90 = ExposedPort.tcp(90);

    private static final Binding BINDING_8080 = Ports.Binding(8080);

    private static final Binding BINDING_9090 = Ports.Binding(9090);

    private Ports ports;

    @BeforeMethod
    public void setup() {
        ports = new Ports();
    }

    @Test
    public void addTwoBindingsForDifferentExposedPorts() {
        ports.add(new PortBinding(BINDING_8080, TCP_80), new PortBinding(BINDING_9090, TCP_90));

        Map<ExposedPort, Binding[]> bindings = ports.getBindings();
        // two keys with one value each
        Assert.assertEquals(bindings.size(), 2);
        Assert.assertEquals(bindings.get(TCP_80), new Binding[] { BINDING_8080 });
        Assert.assertEquals(bindings.get(TCP_90), new Binding[] { BINDING_9090 });
    }

    @Test
    public void addTwoBindingsForSameExposedPort() {
        ports.add(new PortBinding(BINDING_8080, TCP_80), new PortBinding(BINDING_9090, TCP_80));

        Map<ExposedPort, Binding[]> bindings = ports.getBindings();
        // one key with two values
        Assert.assertEquals(bindings.size(), 1);
        Assert.assertEquals(bindings.get(TCP_80), new Binding[] { BINDING_8080, BINDING_9090 });
    }

    @Test
    public void addNullBindings() {
        ports.add(new PortBinding(null, TCP_80));
        Map<ExposedPort, Binding[]> bindings = ports.getBindings();
        // one key with two values
        Assert.assertEquals(bindings.size(), 1);
        Assert.assertEquals(bindings.get(TCP_80), null);
    }
}
