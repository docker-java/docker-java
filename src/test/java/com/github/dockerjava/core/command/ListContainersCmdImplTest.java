package com.github.dockerjava.core.command;

import static ch.lambdaj.Lambda.filter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

import java.lang.reflect.Method;
import java.util.List;

import org.hamcrest.Matcher;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class ListContainersCmdImplTest extends AbstractDockerClientTest {

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
	public void testListContainers() throws DockerException {

		String testImage = "busybox";

		// need to block until image is pulled completely
		asString(dockerClient.pullImageCmd(testImage).exec());

		List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
		assertThat(containers, notNullValue());
		LOG.info("Container List: {}", containers);

		int size = containers.size();

		CreateContainerResponse container1 = dockerClient
				.createContainerCmd(testImage).withCmd("echo").exec();

		assertThat(container1.getId(), not(isEmptyString()));

		InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container1.getId()).exec();

		assertThat(inspectContainerResponse.getConfig().getImage(), is(equalTo(testImage)));

		dockerClient.startContainerCmd(container1.getId()).exec();

		LOG.info("container id: " + container1.getId());

		List<Container> containers2 = dockerClient.listContainersCmd().withShowAll(true).exec();

		for(Container container: containers2) {
			LOG.info("listContainer: id=" + container.getId() +" image=" + container.getImage());
		}

		assertThat(size + 1, is(equalTo(containers2.size())));
		Matcher matcher = hasItem(hasField("id", startsWith(container1.getId())));
		assertThat(containers2, matcher);

		List<Container> filteredContainers = filter(
				hasField("id", startsWith(container1.getId())), containers2);
		assertThat(filteredContainers.size(), is(equalTo(1)));

		for(Container container: filteredContainers) {
			LOG.info("filteredContainer: " + container);
		}

		Container container2 = filteredContainers.get(0);
		assertThat(container2.getCommand(), not(isEmptyString()));
		assertThat(container2.getImage(), startsWith(testImage + ":"));
	}



}
