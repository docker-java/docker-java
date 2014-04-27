package com.kpelykh.docker.client.test;


import com.kpelykh.docker.client.DockerClient;
import com.kpelykh.docker.client.DockerException;
import com.sun.jersey.api.client.UniformInterfaceException;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;

public class DockerClientAuthTest {
	private DockerClient docker;

	@BeforeTest
	public void beforeTest() throws Exception {
		docker = new DockerClient();
	}

	@Test
	public void testAuth() throws Exception {
		docker.setCredentials("alexec", System.getProperty("password"), "alex.e.c@gmail.com");
		docker.auth();
	}

	@Test
	public void testAuthInvalid() throws Exception {
		docker.setCredentials("alexec", "XXXXX", "alex.e.c@gmail.com");
		try {
			docker.auth();
		} catch (DockerException e) {
			assertThat(e.getCause(), Matchers.instanceOf(UniformInterfaceException.class));
			assertEquals(401, ((UniformInterfaceException) e.getCause()).getResponse().getStatus());
		}
	}
}
