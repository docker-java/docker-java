package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.github.dockerjava.api.model.Ports.Binding;

public class BindingTest {

	@Test
	public void parseIpAndPort() {
		assertEquals(Binding.parse("127.0.0.1:80"), Ports.Binding("127.0.0.1", 80));
	}
	
	@Test
	public void parsePortOnly() {
		assertEquals(Binding.parse("80"), Ports.Binding("", 80));
	}

	@Test(expectedExceptions = IllegalArgumentException.class,
			expectedExceptionsMessageRegExp = "Error parsing Binding 'nonsense'")
	public void parseInvalidInput() {
		Binding.parse("nonsense");
	}

	@Test(expectedExceptions = IllegalArgumentException.class, 
			expectedExceptionsMessageRegExp = "Error parsing Binding 'null'")
	public void parseNull() {
		Binding.parse(null);
	}

	@Test
	public void toStringIpAndHost() {
		assertEquals(Binding.parse("127.0.0.1:80").toString(), "127.0.0.1:80");
	}

	@Test
	public void toStringPortOnly() {
		assertEquals(Binding.parse("80").toString(), "80");
	}

}
