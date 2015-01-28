package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.Ports.Binding;

public class VolumeFrom_SerializingTest {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final String json = 
			"[\"container1:ro\",\"container2:rw\"]";
	@Test
	public void deserializing() throws Exception {
		VolumesFrom volumesFrom = objectMapper.readValue(json, VolumesFrom.class);
		
		VolumesFrom expected = new VolumesFrom(new VolumeFrom("container1", AccessMode.ro), new VolumeFrom("container2", AccessMode.rw));
		
		assertEquals(volumesFrom.getVolumesFrom(), expected.getVolumesFrom());
	}
	
	@Test
	public void serializing() throws Exception {
		VolumesFrom volumesFrom = new VolumesFrom(new VolumeFrom("container1", AccessMode.ro), new VolumeFrom("container2", AccessMode.rw));
		
		assertEquals(objectMapper.writeValueAsString(volumesFrom), json);
	}
	
	
}
