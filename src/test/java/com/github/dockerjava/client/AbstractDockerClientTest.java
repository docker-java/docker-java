package com.github.dockerjava.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.DockerException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
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


	public void beforeTest()  {
		LOG.info("======================= BEFORETEST =======================");
		LOG.info("Connecting to Docker server");
		dockerClient = new DockerClientImpl();

		LOG.info("Pulling image 'busybox'");
		// need to block until image is pulled completely
		asString(dockerClient.pullImageCmd("busybox").withTag("latest").exec());
		
		

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
			} catch (DockerException ignore) {
				//ignore.printStackTrace();
			}
			
			try {
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

	protected String asString(InputStream response)  {
	
		StringWriter logwriter = new StringWriter();
        
		try {
			LineIterator itr = IOUtils.lineIterator(
					response, "UTF-8");

			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + (itr.hasNext() ? "\n" : ""));
				//LOG.info("line: "+line);
			}
			
			return logwriter.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(response);
		}
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
