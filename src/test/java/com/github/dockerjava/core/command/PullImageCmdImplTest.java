package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.client.AbstractDockerClientTest;

public class PullImageCmdImplTest extends AbstractDockerClientTest {

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

		InputStream response = dockerClient.pullImageCmd(testImage).exec();

		assertThat(asString(response),
				containsString("Download complete"));

		info = dockerClient.infoCmd().exec();
		LOG.info("Client info after pull, {}", info.toString());

		assertThat(imgCount, lessThanOrEqualTo(info.getImages()));

		InspectImageResponse inspectImageResponse = dockerClient
				.inspectImageCmd(testImage).exec();
		LOG.info("Image Inspect: {}", inspectImageResponse.toString());
		assertThat(inspectImageResponse, notNullValue());
	}

}
