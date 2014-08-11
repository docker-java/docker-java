package com.github.dockerjava.client.command;

import java.lang.reflect.Method;

import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.client.AbstractDockerClientTest;

public class TagImageCommandTest extends AbstractDockerClientTest {

	public static final Logger LOG = LoggerFactory
			.getLogger(TagImageCommandTest.class);

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
	public void testTagImage() throws Exception {
		String tag = String.valueOf(RandomUtils.nextInt(Integer.MAX_VALUE));

		dockerClient.tagImageCmd("busybox:latest", "docker-java/busybox", tag).exec();

		dockerClient.removeImageCmd("docker-java/busybox:" + tag).exec();
	}

}

