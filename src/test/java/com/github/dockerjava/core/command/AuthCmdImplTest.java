package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.DockerClientBuilder;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;

@Test(groups = "integration")
public class AuthCmdImplTest extends AbstractDockerClientTest {

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
        AuthResponse response = dockerClient.authCmd().exec();

        assertEquals(response.getStatus(), "Login Succeeded");
    }

	@Test
	public void testAuthInvalid() throws Exception {

        try {
			DockerClientBuilder.getInstance(config("garbage")).build().authCmd().exec();
            fail("Expected a UnauthorizedException caused by a bad password.");
		} catch (UnauthorizedException e) {
			assertEquals(e.getMessage(), "Wrong login/password, please try again\n");
		} 
	}
}
