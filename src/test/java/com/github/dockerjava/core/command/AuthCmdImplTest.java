package com.github.dockerjava.core.command;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.UnauthorizedException;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs1.DockerClientBuilder;

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
		dockerClient.authCmd().exec();
	}

	@Test
	public void testAuthInvalid() throws Exception {
		DockerClientConfig config = DockerClientConfig.createDefaultConfigBuilder().withPassword("garbage").build();
		DockerClient client = DockerClientBuilder.getInstance(config).withDockerCmdExecFactory(dockerCmdExecFactory).build();
        
		try {
			client.authCmd().exec();
            fail("Expected a UnauthorizedException caused by a bad password.");
		} catch (UnauthorizedException e) {
			
		} 
	}
}
