package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class VolumeTest {
	@Test
	public void stringify() {
		assertEquals(Volume.parse("/path").toString(), "/path");
	}
}
