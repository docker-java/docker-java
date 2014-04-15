package com.kpelykh.docker.client.test;

import com.kpelykh.docker.client.DockerClient;
import com.kpelykh.docker.client.DockerException;
import com.kpelykh.docker.client.model.*;
import com.sun.jersey.api.client.ClientResponse;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;


import static ch.lambdaj.Lambda.filter;
import static ch.lambdaj.Lambda.selectUnique;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

/**
 * Unit test for DockerClient.
 * 
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 */
public class DockerClientTest extends Assert {
	public static final Logger LOG = LoggerFactory
			.getLogger(DockerClientTest.class);

	private DockerClient dockerClient;

	private List<String> tmpImgs = new ArrayList<String>();
	private List<String> tmpContainers = new ArrayList<String>();

	@BeforeTest
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

	@AfterTest
	public void afterTest() {
		LOG.info("======================= END OF AFTERTEST =======================");
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		LOG.info(String
				.format("################################## STARTING %s ##################################",
						method.getName()));
	}

	@AfterMethod
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

	/*
	 * ######################### ## INFORMATION TESTS ##
	 * #########################
	 */

	@Test
	public void testDockerVersion() throws DockerException {
		Version version = dockerClient.version();
		LOG.info(version.toString());

		assertTrue(version.getGoVersion().length() > 0);
		assertTrue(version.getVersion().length() > 0);

		assertEquals(StringUtils.split(version.getVersion(), ".").length, 3);

	}

	@Test
	public void testDockerInfo() throws DockerException {
		Info dockerInfo = dockerClient.info();
		LOG.info(dockerInfo.toString());

		assertTrue(dockerInfo.toString().contains("containers"));
		assertTrue(dockerInfo.toString().contains("images"));
		assertTrue(dockerInfo.toString().contains("debug"));

		assertTrue(dockerInfo.getContainers() > 0);
		assertTrue(dockerInfo.getImages() > 0);
		assertTrue(dockerInfo.getNFd() > 0);
		assertTrue(dockerInfo.getNGoroutines() > 0);
		assertTrue(dockerInfo.isMemoryLimit());
	}

	@Test
	public void testDockerSearch() throws DockerException {
		List<SearchItem> dockerSearch = dockerClient.search("busybox");
		LOG.info("Search returned {}", dockerSearch.toString());

		Matcher matcher = hasItem(hasField("name", equalTo("busybox")));
		assertThat(dockerSearch, matcher);

		assertThat(
				filter(hasField("name", is("busybox")), dockerSearch).size(),
				equalTo(1));
	}

	/*
	 * ################### ## LISTING TESTS ## ###################
	 */

	@Test
	public void testImages() throws DockerException {
		List<Image> images = dockerClient.getImages(true);
		assertThat(images, notNullValue());
		LOG.info("Images List: {}", images);
		Info info = dockerClient.info();

		assertThat(images.size(), equalTo(info.getImages()));

		Image img = images.get(0);
		assertThat(img.getCreated(), is(greaterThan(0L)));
		assertThat(img.getVirtualSize(), is(greaterThan(0L)));
		assertThat(img.getId(), not(isEmptyString()));
		assertThat(img.getTag(), not(isEmptyString()));
		assertThat(img.getRepository(), not(isEmptyString()));
	}

	@Test
	public void testListContainers() throws DockerException {
		List<Container> containers = dockerClient.listContainers(true);
		assertThat(containers, notNullValue());
		LOG.info("Container List: {}", containers);

		int size = containers.size();

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "echo" });

		ContainerCreateResponse container1 = dockerClient
				.createContainer(containerConfig);
		assertThat(container1.getId(), not(isEmptyString()));
		dockerClient.startContainer(container1.getId());
		tmpContainers.add(container1.getId());

		List containers2 = dockerClient.listContainers(true);
		assertThat(size + 1, is(equalTo(containers2.size())));
		Matcher matcher = hasItem(hasField("id", startsWith(container1.getId())));
		assertThat(containers2, matcher);

		List<Container> filteredContainers = filter(
				hasField("id", startsWith(container1.getId())), containers2);
		assertThat(filteredContainers.size(), is(equalTo(1)));

		Container container2 = filteredContainers.get(0);
		assertThat(container2.getCommand(), not(isEmptyString()));
		assertThat(container2.getImage(), equalTo("busybox:latest"));
	}

	/*
	 * ##################### ## CONTAINER TESTS ## #####################
	 */

	@Test
	public void testCreateContainer() throws DockerException {
		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "true" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);

		LOG.info("Created container {}", container.toString());

		assertThat(container.getId(), not(isEmptyString()));

		tmpContainers.add(container.getId());
	}

	@Test
	public void testStartContainer() throws DockerException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "true" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		boolean add = tmpContainers.add(container.getId());

		dockerClient.startContainer(container.getId());

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainer(container.getId());
		LOG.info("Container Inspect: {}", containerInspectResponse.toString());

		assertThat(containerInspectResponse.config, is(notNullValue()));
		assertThat(containerInspectResponse.getId(), not(isEmptyString()));

		assertThat(containerInspectResponse.getId(),
				startsWith(container.getId()));

		assertThat(containerInspectResponse.getImage(), not(isEmptyString()));
		assertThat(containerInspectResponse.getState(), is(notNullValue()));

		assertThat(containerInspectResponse.getState().running, is(true));

		if (!containerInspectResponse.getState().running) {
			assertThat(containerInspectResponse.getState().exitCode,
					is(equalTo(0)));
		}

	}

	@Test
	public void testWaitContainer() throws DockerException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "true" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		tmpContainers.add(container.getId());

		dockerClient.startContainer(container.getId());

		int exitCode = dockerClient.waitContainer(container.getId());
		LOG.info("Container exit code: {}", exitCode);

		assertThat(exitCode, equalTo(0));

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainer(container.getId());
		LOG.info("Container Inspect: {}", containerInspectResponse.toString());

		assertThat(containerInspectResponse.getState().running,
				is(equalTo(false)));
		assertThat(containerInspectResponse.getState().exitCode,
				is(equalTo(exitCode)));

	}

	@Test
	public void testLogs() throws DockerException, IOException {

		String snippet = "hello world";

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "/bin/echo", snippet });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainer(container.getId());
		tmpContainers.add(container.getId());

		int exitCode = dockerClient.waitContainer(container.getId());

		assertThat(exitCode, equalTo(0));

		ClientResponse response = dockerClient.logContainer(container.getId());

		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					response.getEntityInputStream(), "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + (itr.hasNext() ? "\n" : ""));
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(response.getEntityInputStream());
		}

		String fullLog = logwriter.toString();

		LOG.info("Container log: {}", fullLog);
		assertThat(fullLog, endsWith(snippet));
	}

	@Test
	public void testCopyFile() throws DockerException, IOException {

		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testReadFile").getFile());
		String containerId = dockerfileBuild(baseDir, "hello.txt Created");

		String fileName = "hello.txt";
		String fileText = "Hello Word";

		InputStream response = dockerClient.copyFile(containerId, fileName);
		TarArchiveInputStream files  = new TarArchiveInputStream(response);
		
		TarArchiveEntry entry = files.getNextTarEntry();
		
		byte[] content = new byte[(int) entry.getSize()];		
		files.read(content, 0, content.length);
		

		String fullLog = new String(content);

		LOG.info("Read File: {}", fullLog);
		assertThat(fullLog.trim(), endsWith(fileText));
	}

	@Test
	public void testDiff() throws DockerException {
		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "touch", "/test" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainer(container.getId());
		boolean add = tmpContainers.add(container.getId());
		int exitCode = dockerClient.waitContainer(container.getId());
		assertThat(exitCode, equalTo(0));

		List filesystemDiff = dockerClient.containterDiff(container.getId());
		LOG.info("Container DIFF: {}", filesystemDiff.toString());

		assertThat(filesystemDiff.size(), equalTo(4));
		ChangeLog testChangeLog = selectUnique(filesystemDiff,
				hasField("path", equalTo("/test")));

		assertThat(testChangeLog, hasField("path", equalTo("/test")));
		assertThat(testChangeLog, hasField("kind", equalTo(1)));
	}

	@Test
	public void testStopContainer() throws DockerException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "sleep", "9999" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainer(container.getId());
		tmpContainers.add(container.getId());

		LOG.info("Stopping container: {}", container.getId());
		dockerClient.stopContainer(container.getId(), 2);

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainer(container.getId());
		LOG.info("Container Inspect: {}", containerInspectResponse.toString());

		assertThat(containerInspectResponse.getState().running,
				is(equalTo(false)));
		assertThat(containerInspectResponse.getState().exitCode,
				not(equalTo(0)));
	}

	@Test
	public void testKillContainer() throws DockerException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "sleep", "9999" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainer(container.getId());
		tmpContainers.add(container.getId());

		LOG.info("Killing container: {}", container.getId());
		dockerClient.kill(container.getId());

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainer(container.getId());
		LOG.info("Container Inspect: {}", containerInspectResponse.toString());

		assertThat(containerInspectResponse.getState().running,
				is(equalTo(false)));
		assertThat(containerInspectResponse.getState().exitCode,
				not(equalTo(0)));

	}

	@Test
	public void restartContainer() throws DockerException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "sleep", "9999" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainer(container.getId());
		tmpContainers.add(container.getId());

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainer(container.getId());
		LOG.info("Container Inspect: {}", containerInspectResponse.toString());

		String startTime = containerInspectResponse.getState().startedAt;

		dockerClient.restart(container.getId(), 2);

		ContainerInspectResponse containerInspectResponse2 = dockerClient
				.inspectContainer(container.getId());
		LOG.info("Container Inspect After Restart: {}",
				containerInspectResponse2.toString());

		String startTime2 = containerInspectResponse2.getState().startedAt;

		assertThat(startTime, not(equalTo(startTime2)));

		assertThat(containerInspectResponse.getState().running,
				is(equalTo(true)));

		dockerClient.kill(container.getId());
	}

	@Test
	public void removeContainer() throws DockerException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "true" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);

		dockerClient.startContainer(container.getId());
		dockerClient.waitContainer(container.getId());
		tmpContainers.add(container.getId());

		LOG.info("Removing container: {}", container.getId());
		dockerClient.removeContainer(container.getId());

		List containers2 = dockerClient.listContainers(true);
		Matcher matcher = not(hasItem(hasField("id",
				startsWith(container.getId()))));
		assertThat(containers2, matcher);

	}

	/*
	 * ################## ## IMAGES TESTS ## ##################
	 */

	@Test
	public void testPullImage() throws DockerException, IOException {

		String testImage = "kpelykh/vimbase";

		LOG.info("Removing image: {}", testImage);
		dockerClient.removeImage(testImage);

		Info info = dockerClient.info();
		LOG.info("Client info: {}", info.toString());

		int imgCount = info.getImages();

		LOG.info("Pulling image: {}", testImage);

		ClientResponse response = dockerClient.pull(testImage);

		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					response.getEntityInputStream(), "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + "\n");
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(response.getEntityInputStream());
		}

		String fullLog = logwriter.toString();
		assertThat(fullLog,
				containsString("Pulling repository kpelykh/vimbase"));

		tmpImgs.add(testImage);

		info = dockerClient.info();
		LOG.info("Client info after pull, {}", info.toString());

		assertThat(imgCount + 1, equalTo(info.getImages()));

		ImageInspectResponse imageInspectResponse = dockerClient
				.inspectImage(testImage);
		LOG.info("Image Inspect: {}", imageInspectResponse.toString());
		assertThat(imageInspectResponse, notNullValue());
	}

	@Test
	public void commitImage() throws DockerException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "touch", "/test" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainer(container.getId());
		tmpContainers.add(container.getId());

		LOG.info("Commiting container: {}", container.toString());
		String imageId = dockerClient
				.commit(new CommitConfig(container.getId()));
		tmpImgs.add(imageId);

		ImageInspectResponse imageInspectResponse = dockerClient
				.inspectImage(imageId);
		LOG.info("Image Inspect: {}", imageInspectResponse.toString());

		assertThat(imageInspectResponse,
				hasField("container", startsWith(container.getId())));
		assertThat(imageInspectResponse.getContainerConfig().getImage(),
				equalTo("busybox"));

		ImageInspectResponse busyboxImg = dockerClient.inspectImage("busybox");

		assertThat(imageInspectResponse.getParent(),
				equalTo(busyboxImg.getId()));
	}

	@Test
	public void testRemoveImage() throws DockerException, InterruptedException {

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage("busybox");
		containerConfig.setCmd(new String[] { "touch", "/test" });

		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainer(container.getId());
		tmpContainers.add(container.getId());

		LOG.info("Commiting container {}", container.toString());
		String imageId = dockerClient
				.commit(new CommitConfig(container.getId()));
		tmpImgs.add(imageId);

		dockerClient.stopContainer(container.getId());
		dockerClient.kill(container.getId());
		dockerClient.removeContainer(container.getId());

		tmpContainers.remove(container.getId());
		LOG.info("Removing image: {}", imageId);
		dockerClient.removeImage(imageId);

		List containers = dockerClient.listContainers(true);
		Matcher matcher = not(hasItem(hasField("id", startsWith(imageId))));
		assertThat(containers, matcher);
	}

	/*
	 * 
	 * ################ ## MISC TESTS ## ################
	 */

	@Test
	public void testRunShlex() throws DockerException {

		String[] commands = new String[] {
				"true",
				"echo \"The Young Descendant of Tepes & Septette for the Dead Princess\"",
				"echo -n 'The Young Descendant of Tepes & Septette for the Dead Princess'",
				"/bin/sh -c echo Hello World", "/bin/sh -c echo 'Hello World'",
				"echo 'Night of Nights'", "true && echo 'Night of Nights'" };

		for (String command : commands) {
			LOG.info("Running command: [{}]", command);

			ContainerConfig containerConfig = new ContainerConfig();
			containerConfig.setImage("busybox");
			containerConfig.setCmd(commands);

			ContainerCreateResponse container = dockerClient
					.createContainer(containerConfig);
			dockerClient.startContainer(container.getId());
			tmpContainers.add(container.getId());
			int exitcode = dockerClient.waitContainer(container.getId());
			assertThat(exitcode, equalTo(0));
		}
	}

	@Test
	public void testNginxDockerfileBuilder() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("nginx").getFile());

		ClientResponse response = dockerClient.build(baseDir);

		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					response.getEntityInputStream(), "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + "\n");
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(response.getEntityInputStream());
		}

		String fullLog = logwriter.toString();
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		ImageInspectResponse imageInspectResponse = dockerClient
				.inspectImage(imageId);
		assertThat(imageInspectResponse, not(nullValue()));
		LOG.info("Image Inspect: {}", imageInspectResponse.toString());
		tmpImgs.add(imageInspectResponse.getId());

		assertThat(imageInspectResponse.getAuthor(),
				equalTo("Guillaume J. Charmes \"guillaume@dotcloud.com\""));
	}

	@Test
	public void testDockerBuilderAddFile() throws DockerException, IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddFile").getFile());
		dockerfileBuild(baseDir, "Successfully executed testrun.sh");
	}

	@Test
	public void testDockerBuilderAddFolder() throws DockerException,
			IOException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("testAddFolder").getFile());
		dockerfileBuild(baseDir, "Successfully executed testAddFolder.sh");
	}

	@Test
	public void testNetCatDockerfileBuilder() throws DockerException,
			IOException, InterruptedException {
		File baseDir = new File(Thread.currentThread().getContextClassLoader()
				.getResource("netcat").getFile());

		ClientResponse response = dockerClient.build(baseDir);

		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					response.getEntityInputStream(), "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + "\n");
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(response.getEntityInputStream());
		}

		String fullLog = logwriter.toString();
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		ImageInspectResponse imageInspectResponse = dockerClient
				.inspectImage(imageId);
		assertThat(imageInspectResponse, not(nullValue()));
		LOG.info("Image Inspect: {}", imageInspectResponse.toString());
		tmpImgs.add(imageInspectResponse.getId());

		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage(imageInspectResponse.getId());
		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		assertThat(container.getId(), not(isEmptyString()));
		dockerClient.startContainer(container.getId());
		tmpContainers.add(container.getId());

		ContainerInspectResponse containerInspectResponse = dockerClient
				.inspectContainer(container.getId());

		assertThat(containerInspectResponse.getId(), notNullValue());
		assertThat(containerInspectResponse.getNetworkSettings().ports,
				notNullValue());

		// No use as such if not running on the server
		for (String portstr : containerInspectResponse.getNetworkSettings().ports
				.getAllPorts().keySet()) {

			Ports.Port p = containerInspectResponse.getNetworkSettings().ports
					.getAllPorts().get(portstr);
			int port = Integer.valueOf(p.getHostPort());
			LOG.info("Checking port {} is open", port);
			assertThat(available(port), is(false));
		}
		dockerClient.stopContainer(container.getId(), 0);

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

	private String dockerfileBuild(File baseDir, String expectedText)
			throws DockerException, IOException {

		// Build image
		ClientResponse response = dockerClient.build(baseDir);

		StringWriter logwriter = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					response.getEntityInputStream(), "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter.write(line + "\n");
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(response.getEntityInputStream());
		}

		String fullLog = logwriter.toString();
		assertThat(fullLog, containsString("Successfully built"));

		String imageId = StringUtils.substringBetween(fullLog,
				"Successfully built ", "\\n\"}").trim();

		// Create container based on image
		ContainerConfig containerConfig = new ContainerConfig();
		containerConfig.setImage(imageId);
		ContainerCreateResponse container = dockerClient
				.createContainer(containerConfig);
		LOG.info("Created container: {}", container.toString());
		assertThat(container.getId(), not(isEmptyString()));

		dockerClient.startContainer(container.getId());
		dockerClient.waitContainer(container.getId());

		tmpContainers.add(container.getId());

		// Log container
		ClientResponse logResponse = dockerClient.logContainer(container
				.getId());

		StringWriter logwriter2 = new StringWriter();

		try {
			LineIterator itr = IOUtils.lineIterator(
					logResponse.getEntityInputStream(), "UTF-8");
			while (itr.hasNext()) {
				String line = itr.next();
				logwriter2.write(line + (itr.hasNext() ? "\n" : ""));
				LOG.info(line);
			}
		} finally {
			IOUtils.closeQuietly(logResponse.getEntityInputStream());
		}

		assertThat(logwriter2.toString(), endsWith(expectedText));

		return container.getId();
	}
}