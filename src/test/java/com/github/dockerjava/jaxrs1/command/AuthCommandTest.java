package com.github.dockerjava.jaxrs1.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.jaxrs1.JaxRs1Client;
import com.sun.jersey.api.client.UniformInterfaceException;

import org.hamcrest.Matchers;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;

public class AuthCommandTest extends AbstractDockerClientTest {

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
		dockerClient.authCmd().exec();
	}

	@Test
	public void testAuthInvalid() throws Exception {
        System.setProperty("docker.io.password", "garbage");
		try {
			new JaxRs1Client().authCmd().exec();
            fail("Expected a DockerException caused by a bad password.");
		} catch (DockerException e) {
			assertThat(e.getCause(), Matchers.instanceOf(UniformInterfaceException.class));
			assertEquals(((UniformInterfaceException) e.getCause()).getResponse().getStatus(), 401);
		}
	}
}
