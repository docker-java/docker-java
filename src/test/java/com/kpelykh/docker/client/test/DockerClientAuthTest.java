package com.kpelykh.docker.client.test;

import com.kpelykh.docker.client.DockerException;
import com.sun.jersey.api.client.UniformInterfaceException;

import org.hamcrest.Matchers;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;

public class DockerClientAuthTest extends AbstractDockerClientTest {

	@BeforeTest
	public void beforeTest() throws DockerException  {
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
		dockerClient.auth();
	}

	@Test
	public void testAuthInvalid() throws Exception {
        System.setProperty("docker.io.password", "garbage");
		try {
			dockerClient.auth();
            fail();
		} catch (DockerException e) {
			assertThat(e.getCause(), Matchers.instanceOf(UniformInterfaceException.class));
			assertEquals(((UniformInterfaceException) e.getCause()).getResponse().getStatus(), 401);
		}
	}
}
