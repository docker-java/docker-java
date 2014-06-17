package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.ImageInspectResponse;
import com.github.dockerjava.client.model.Info;
import com.sun.jersey.api.client.ClientResponse;

public class PullImageCmdTest extends AbstractDockerClientTest {

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
	public void testPullImage() throws DockerException, IOException {
		Info info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());
		
		int imgCount = info.getImages();
		LOG.info("imgCount1: {}", imgCount);

		// This should be an image that is not used by other repositories
		// already
		// pulled down, preferably small in size. If tag is not used pull will
		// download all images in that repository but tmpImgs will only
		// deleted 'latest' image but not images with other tags
		String testImage = "hackmann/empty";

		LOG.info("Removing image: {}", testImage);
		dockerClient.removeImageCmd(testImage).exec();

		info = dockerClient.infoCmd().exec();
		LOG.info("Client info: {}", info.toString());
		
		imgCount = info.getImages();
		LOG.info("imgCount2: {}", imgCount);
		

		LOG.info("Pulling image: {}", testImage);

		tmpImgs.add(testImage);
		ClientResponse response = dockerClient.pullImageCmd(testImage).exec();

		assertThat(logResponseStream(response),
				containsString("Download complete"));

		info = dockerClient.infoCmd().exec();
		LOG.info("Client info after pull, {}", info.toString());

		assertThat(imgCount, lessThanOrEqualTo(info.getImages()));

		ImageInspectResponse imageInspectResponse = dockerClient
				.inspectImageCmd(testImage).exec();
		LOG.info("Image Inspect: {}", imageInspectResponse.toString());
		assertThat(imageInspectResponse, notNullValue());
	}

}
