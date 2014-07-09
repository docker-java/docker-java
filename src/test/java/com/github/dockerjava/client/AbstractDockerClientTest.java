package com.github.dockerjava.client;

import com.github.dockerjava.client.DockerClient;
import com.github.dockerjava.client.DockerException;
import com.sun.jersey.api.client.ClientResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.ServerSocket;
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
		LOG.info("Connecting to Docker server");
		dockerClient = new DockerClient();

		LOG.info("Pulling image 'busybox'");
		// need to block until image is pulled completely
		logResponseStream(dockerClient.pullImageCmd("busybox:latest").exec());
		
		

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
				dockerClient.stopContainerCmd(container).exec();
				dockerClient.killContainerCmd(container).exec();
				dockerClient.removeContainerCmd(container).exec();
			} catch (DockerException ignore) {
				ignore.printStackTrace();
			}
		}

		for (String image : tmpImgs) {
			LOG.info("Cleaning up temporary image {}", image);
			try {
				dockerClient.removeImageCmd(image).exec();
			} catch (DockerException ignore) {
				ignore.printStackTrace();
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
	
	// UTIL

	/**
	 * Checks to see if a specific port is available.
	 * 
	 * @param port
	 *            the port to check for availability
	 */
	public static boolean available(int port) {
		if (port < 1100 || port > 60000) {
			throw new IllegalArgumentException("Invalid start port: " + port);
		}

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}

}
