package com.github.dockerjava.client.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Method;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.client.DockerException;
import com.github.dockerjava.client.model.Image;
import com.github.dockerjava.client.model.Info;

public class ListImagesCmdTest extends AbstractDockerClientTest {

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
	public void listImages() throws DockerException {
		List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();
		assertThat(images, notNullValue());
		LOG.info("Images List: {}", images);
		Info info = dockerClient.infoCmd().exec();

		assertThat(images.size(), equalTo(info.getImages()));

		Image img = images.get(0);
		assertThat(img.getCreated(), is(greaterThan(0L)));
		assertThat(img.getVirtualSize(), is(greaterThan(0L)));
		assertThat(img.getId(), not(isEmptyString()));
		assertThat(img.getTag(), not(isEmptyString()));
		assertThat(img.getRepository(), not(isEmptyString()));
	}


}
