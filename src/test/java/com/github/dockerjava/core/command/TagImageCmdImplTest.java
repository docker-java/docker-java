package com.github.dockerjava.core.command;

import java.lang.reflect.Method;

import com.github.dockerjava.core.util.DockerImageName;
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
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class TagImageCmdImplTest extends AbstractDockerClientTest {

	public static final Logger LOG = LoggerFactory
			.getLogger(TagImageCmdImplTest.class);

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
	public void tagImage() throws Exception {
		String tag = "" + RandomUtils.nextInt(Integer.MAX_VALUE);

		dockerClient.tagImageCmd(new DockerImageName("busybox:latest"), new DockerImageName("docker-java/busybox"+ tag)).exec();

		dockerClient.removeImageCmd(new DockerImageName("docker-java/busybox:" + tag)).exec();
	}
	
	@Test
	public void tagNonExistingImage() throws Exception {
		String tag = "" + RandomUtils.nextInt(Integer.MAX_VALUE);
		
		try {
			dockerClient.tagImageCmd(new DockerImageName("non-existing"), new DockerImageName("docker-java/busybox"+ tag)).exec();
			fail("expected NotFoundException");
		} catch (NotFoundException e) {
		}	
	}

}

