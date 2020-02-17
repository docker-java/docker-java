package com.github.dockerjava.api.model;

import com.github.dockerjava.api.model.Ports.Binding;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * As there may be several {@link Binding}s per {@link ExposedPort}, it makes a difference if you add {@link PortBinding}s for the same or
 * different {@link ExposedPort}s to {@link Ports}. This test verifies that the Map in {@link Ports} is populated correctly in both cases.
 */
public class PortsAddBindingsTest {
    private static final ExposedPort TCP_80 = ExposedPort.tcp(80);

    private static final ExposedPort TCP_90 = ExposedPort.tcp(90);

    private static final Binding BINDING_8080 = Binding.bindPort(8080);

    private static final Binding BINDING_9090 = Binding.bindPort(9090);

    private Ports ports;

    @Before
    public void setup() {
        ports = new Ports();
    }

    @Test
    public void addTwoBindingsForDifferentExposedPorts() {
        ports.add(new PortBinding(BINDING_8080, TCP_80), new PortBinding(BINDING_9090, TCP_90));

        Map<ExposedPort, Binding[]> bindings = ports.getBindings();
        // two keys with one value each
        assertEquals(bindings.size(), 2);
        assertArrayEquals(bindings.get(TCP_80), new Binding[] {BINDING_8080});
        assertArrayEquals(bindings.get(TCP_90), new Binding[] {BINDING_9090});
    }

    @Test
    public void addTwoBindingsForSameExposedPort() {
        ports.add(new PortBinding(BINDING_8080, TCP_80), new PortBinding(BINDING_9090, TCP_80));

        Map<ExposedPort, Binding[]> bindings = ports.getBindings();
        // one key with two values
        assertEquals(bindings.size(), 1);
        assertArrayEquals(bindings.get(TCP_80), new Binding[] {BINDING_8080, BINDING_9090});
    }

    @Test
    public void addNullBindings() {
        ports.add(new PortBinding(null, TCP_80));
        Map<ExposedPort, Binding[]> bindings = ports.getBindings();
        // one key with two values
        assertEquals(bindings.size(), 1);
        assertNull(bindings.get(TCP_80));
    }
}
