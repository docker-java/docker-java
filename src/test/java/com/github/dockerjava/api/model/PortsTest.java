package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Ports.Binding;

public class PortsTest {
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void deserializingPortWithMultipleBindingsReturnsFirstBinding() throws Exception {
		String json = "{\"80/tcp\":[{\"HostIp\":\"10.0.0.1\",\"HostPort\":\"80\"},{\"HostIp\":\"10.0.0.2\",\"HostPort\":\"80\"}]}";
		Ports ports = objectMapper.readValue(json, Ports.class);
		Map<ExposedPort, Binding> bindings = ports.getBindings();
		assertEquals(bindings.size(), 1);

		Binding binding = bindings.get(ExposedPort.tcp(80));
		assertEquals(binding, new Binding("10.0.0.1", 80));
	}
	
	@Test
	public void serializePort() throws Exception {
		Ports ports = new Ports(ExposedPort.tcp(80), new Binding("10.0.0.1", 80));
		String expectedJson = "{\"80/tcp\":[{\"HostIp\":\"10.0.0.1\",\"HostPort\":\"80\"}]}";
		assertEquals(objectMapper.writeValueAsString(ports), expectedJson);
	}
	
}
