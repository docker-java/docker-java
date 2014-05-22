package com.kpelykh.docker.client.test;

import com.kpelykh.docker.client.DockerClient;
import com.kpelykh.docker.client.DockerException;
import com.sun.jersey.api.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
		// need to block until image is pulled completely
		logResponseStream(dockerClient.pull("busybox"));

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

	protected String logResponseStream(ClientResponse response)  {
		String responseString;
		try {
			responseString = DockerClient.asString(response);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		LOG.info("Container log: {}", responseString);
		return responseString;
	}

}
