package com.github.dockerjava.jaxrs.jersey.client.command;

import ch.lambdaj.Lambda;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Filters;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.jaxrs.jersey.client.client.AbstractDockerClientTest;
import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.testinfected.hamcrest.jpa.HasFieldWithValue;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@Test(groups = "integration")
public class ListContainersCmdImplTest extends AbstractDockerClientTest {

    @BeforeTest
    public void beforeTest() throws Exception {
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

    public void testListContainers() throws Exception {

        String testImage = "busybox";

        // need to block until image is pulled completely
        dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitSuccess();

        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        assertThat(containers, Matchers.notNullValue());
        AbstractDockerClientTest.LOG.info("Container List: {}", containers);

        int size = containers.size();

        CreateContainerResponse container1 = dockerClient.createContainerCmd(testImage).withCmd("echo").exec();

        assertThat(container1.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container1.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getImage(), Matchers.is(Matchers.equalTo(testImage)));

        dockerClient.startContainerCmd(container1.getId()).exec();

        AbstractDockerClientTest.LOG.info("container id: " + container1.getId());

        List<Container> containers2 = dockerClient.listContainersCmd().withShowAll(true).exec();

        for (Container container : containers2) {
            AbstractDockerClientTest.LOG.info("listContainer: id=" + container.getId() + " image=" + container.getImage());
        }

        assertThat(size + 1, Matchers.is(Matchers.equalTo(containers2.size())));
        Matcher matcher = Matchers.hasItem(HasFieldWithValue.hasField("id", Matchers.startsWith(container1.getId())));
        assertThat(containers2, matcher);

        List<Container> filteredContainers = Lambda.filter(HasFieldWithValue.hasField("id", Matchers.startsWith(container1.getId())), containers2);
        assertThat(filteredContainers.size(), Matchers.is(Matchers.equalTo(1)));

        for (Container container : filteredContainers) {
            AbstractDockerClientTest.LOG.info("filteredContainer: " + container);
        }

        Container container2 = filteredContainers.get(0);
        assertThat(container2.getCommand(), Matchers.not(Matchers.isEmptyString()));
        assertThat(container2.getImage(), Matchers.startsWith(testImage));
    }

    @Test
    public void testListContainersWithLabelsFilter() throws Exception {

        String testImage = "busybox";

        // need to block until image is pulled completely
        dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitCompletion();

        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        assertThat(containers, Matchers.notNullValue());
        AbstractDockerClientTest.LOG.info("Container List: {}", containers);

        int size = containers.size();

        CreateContainerResponse container1 = dockerClient.createContainerCmd(testImage).withCmd("echo").exec();

        assertThat(container1.getId(), Matchers.not(Matchers.isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container1.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getImage(), Matchers.is(Matchers.equalTo(testImage)));

        dockerClient.startContainerCmd(container1.getId()).exec();

        AbstractDockerClientTest.LOG.info("container id: " + container1.getId());

        List<Container> containers2 = dockerClient.listContainersCmd().withShowAll(true).exec();

        for (Container container : containers2) {
            AbstractDockerClientTest.LOG.info("listContainer: id=" + container.getId() + " image=" + container.getImage());
        }

        assertThat(size + 1, Matchers.is(Matchers.equalTo(containers2.size())));
        Matcher matcher = Matchers.hasItem(HasFieldWithValue.hasField("id", Matchers.startsWith(container1.getId())));
        assertThat(containers2, matcher);

        List<Container> filteredContainers = Lambda.filter(HasFieldWithValue.hasField("id", Matchers.startsWith(container1.getId())), containers2);
        assertThat(filteredContainers.size(), Matchers.is(Matchers.equalTo(1)));

        for (Container container : filteredContainers) {
            AbstractDockerClientTest.LOG.info("filteredContainer: " + container);
        }

        Container container2 = filteredContainers.get(0);
        assertThat(container2.getCommand(), Matchers.not(Matchers.isEmptyString()));
        assertThat(container2.getImage(), Matchers.startsWith(testImage));

        Map<String, String> labels = ImmutableMap.of("test", "docker-java");

        // list with filter by label
        dockerClient.createContainerCmd(testImage).withCmd("echo").withLabels(labels).exec();
        filteredContainers = dockerClient.listContainersCmd().withShowAll(true)
                .withFilters(new Filters().withLabels(labels)).exec();
        assertThat(filteredContainers.size(), Matchers.is(Matchers.equalTo(1)));
        Container container3 = filteredContainers.get(0);
        assertThat(container3.getCommand(), Matchers.not(Matchers.isEmptyString()));
        assertThat(container3.getImage(), Matchers.startsWith(testImage));

        filteredContainers = dockerClient.listContainersCmd().withShowAll(true)
                .withFilters(new Filters().withLabels("test")).exec();
        assertThat(filteredContainers.size(), Matchers.is(Matchers.equalTo(1)));
        container3 = filteredContainers.get(0);
        assertThat(container3.getCommand(), Matchers.not(Matchers.isEmptyString()));
        assertThat(container3.getImage(), Matchers.startsWith(testImage));
        assertEquals(container3.getLabels(), labels);
    }

}
