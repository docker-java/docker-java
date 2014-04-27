package com.kpelykh.docker.client.test;


import com.kpelykh.docker.client.DockerClient;
import com.kpelykh.docker.client.DockerException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.kpelykh.docker.client.DockerClient.asString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

// delete here : https://index.docker.io/u/alexec/busybox/delete/
public class DockerPushTest {
	private DockerClient docker;

	@BeforeTest
	public void beforeTest() throws Exception {
		docker = new DockerClient();
	}

	@Test
	public void testPushLatest() throws Exception {
		setUpCredentials();
		docker.push("busybox");
		assertThat(asString(docker.pull("alexec/busybox")), not(containsString("404")));
	}

	@Test(expectedExceptions = DockerException.class)
	public void testInvalidName() throws Exception {
		setUpCredentials();
		docker.push("XXX");
	}

	@Test
	public void testNotExistentImage() throws Exception {
		setUpCredentials();
		docker.push("alexec/xxx");
		assertThat(asString(docker.pull("alexec/xxx")), containsString("404"));
	}

	private void setUpCredentials() {
		docker.setCredentials("alexec", System.getProperty("password"), "alex.e.c@gmail.com");
	}
}
