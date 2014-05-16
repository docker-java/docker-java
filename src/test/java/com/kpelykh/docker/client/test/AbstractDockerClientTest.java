package com.kpelykh.docker.client.test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;

import com.kpelykh.docker.client.DockerClient;
import com.kpelykh.docker.client.DockerException;
import com.sun.jersey.api.client.ClientResponse;

public abstract class AbstractDockerClientTest extends Assert {
	
	public static final Logger LOG = LoggerFactory
			.getLogger(AbstractDockerClientTest.class);
	
	protected DockerClient dockerClient;

	protected List<String> tmpImgs;
	protected List<String> tmpContainers;


	public void beforeTest() throws DockerException {
		LOG.info("======================= BEFORETEST =======================");
		String url = System.getProperty("docker.url", "http://localhost:4243");
		LOG.info("Connecting to Docker server at " + url);
		dockerClient = new DockerClient(url);

		LOG.info("Creating image 'busybox'");
		dockerClient.pull("busybox");

		assertNotNull(dockerClient);
		LOG.info("======================= END OF BEFORETEST =======================\n\n");
	}

	public void afterTest() {
		LOG.info("======================= END OF AFTERTEST =======================");
	}


	public void beforeMethod(Method method) {
	        tmpContainers = new ArrayList<String>();
	        tmpImgs = new ArrayList<String>();
		LOG.info(String
				.format("################################## STARTING %s ##################################",
						method.getName()));
	}

	public void afterMethod(ITestResult result) {

		for (String container : tmpContainers) {
			LOG.info("Cleaning up temporary container {}", container);
			try {
				dockerClient.stopContainer(container);
				dockerClient.kill(container);
				dockerClient.removeContainer(container);
			} catch (DockerException ignore) {
			}
		}

		for (String image : tmpImgs) {
			LOG.info("Cleaning up temporary image {}", image);
			try {
				dockerClient.removeImage(image);
			} catch (DockerException ignore) {
			}
		}

		LOG.info(
				"################################## END OF {} ##################################\n",
				result.getName());
	}

	protected String logResponseStream(ClientResponse response) throws IOException {
		String responseString = DockerClient.asString(response);
		LOG.info("Container log: {}", responseString);
		return responseString;
	}
	
	private String getProperty(String name) {
		String property = System.getProperty(name);
		if(property == null || property.isEmpty()) throw new RuntimeException("Need to configure '" + name + "' property to run the test. Use command line option -D"+ name +"=... to do so.");
		return property;
	}
	
	protected String getUsername() {
		return getProperty("docker.io.username");
	}
	
	protected String getPassword() {
		return getProperty("docker.io.password");
	}
	
	protected String getEmail() {
		return getProperty("docker.io.email");
	}

}
