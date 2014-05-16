package com.kpelykh.docker.client.test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.kpelykh.docker.client.DockerException;
import com.sun.jersey.api.client.UniformInterfaceException;

public class DockerClientAuthTest extends AbstractDockerClientTest {

	@BeforeTest
	public void beforeTest() throws DockerException {
		super.beforeTest();
	}
	@AfterTest
	public void afterTest() {
		super.afterTest();
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
	    super.beforeMethod(method);
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		super.afterMethod(result);
	}
	
	@Test
	public void testAuth() throws Exception {
		dockerClient.setCredentials(getUsername(), getPassword(), getEmail());
		dockerClient.auth();
	}

	@Test
	public void testAuthInvalid() throws Exception {
		dockerClient.setCredentials(getUsername(), getPassword(), getEmail());
		try {
			dockerClient.auth();
		} catch (DockerException e) {
			assertThat(e.getCause(), Matchers.instanceOf(UniformInterfaceException.class));
			assertEquals(401, ((UniformInterfaceException) e.getCause()).getResponse().getStatus());
		}
	}
}
