package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ExposedPortsTest {

    @Test
    public void usesToJson() throws Exception {
        ExposedPorts ports = new ExposedPorts(
                new ExposedPort(80),
                new ExposedPort(123, InternetProtocol.UDP)
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(ports);

        assertThat(json, is("{\"80/tcp\":{},\"123/udp\":{}}"));
    }

    @Test
    public void usesFromJson() throws Exception {
        ExposedPorts ports = JSONTestHelper.getMapper().readValue("{\"80/tcp\":{},\"123/udp\":{}}", ExposedPorts.class);

        assertThat(ports, notNullValue());
        assertThat(ports.getExposedPorts(), arrayContaining(
                new ExposedPort(80),
                new ExposedPort(123, InternetProtocol.UDP)
        ));
    }
}
