package com.github.dockerjava.netty.exec;

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
import java.util.Map;

import org.hamcrest.Matcher;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.util.FiltersBuilder;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import com.google.common.collect.ImmutableMap;

@Test(groups = "integration")
public class ListContainersCmdExecTest extends AbstractNettyDockerClientTest {

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

    @Test
    public void testListContainers() throws Exception {

        String testImage = "busybox";

        // // need to block until image is pulled completely
        // dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitSuccess();

        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        assertThat(containers, notNullValue());
        LOG.info("Container List: {}", containers);

        int size = containers.size();

        CreateContainerResponse container1 = dockerClient.createContainerCmd(testImage).withCmd("echo").exec();

        assertThat(container1.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container1.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getImage(), is(equalTo(testImage)));

        dockerClient.startContainerCmd(container1.getId()).exec();

        LOG.info("container id: " + container1.getId());

        List<Container> containers2 = dockerClient.listContainersCmd().withShowAll(true).exec();

        for (Container container : containers2) {
            LOG.info("listContainer: id=" + container.getId() + " image=" + container.getImage());
        }

        assertThat(size + 1, is(equalTo(containers2.size())));
        Matcher matcher = hasItem(hasField("id", startsWith(container1.getId())));
        assertThat(containers2, matcher);

        List<Container> filteredContainers = filter(hasField("id", startsWith(container1.getId())), containers2);
        assertThat(filteredContainers.size(), is(equalTo(1)));

        for (Container container : filteredContainers) {
            LOG.info("filteredContainer: " + container);
        }

        Container container2 = filteredContainers.get(0);
        assertThat(container2.getCommand(), not(isEmptyString()));
        assertThat(container2.getImage(), startsWith(testImage));
    }

    @Test
    public void testListContainersWithLabelsFilter() throws Exception {

        String testImage = "busybox";

        // need to block until image is pulled completely
        dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitCompletion();

        List<Container> containers = dockerClient.listContainersCmd().withShowAll(true).exec();
        assertThat(containers, notNullValue());
        LOG.info("Container List: {}", containers);

        int size = containers.size();

        CreateContainerResponse container1 = dockerClient.createContainerCmd(testImage).withCmd("echo").exec();

        assertThat(container1.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(container1.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getImage(), is(equalTo(testImage)));

        dockerClient.startContainerCmd(container1.getId()).exec();

        LOG.info("container id: " + container1.getId());

        List<Container> containers2 = dockerClient.listContainersCmd().withShowAll(true).exec();

        for (Container container : containers2) {
            LOG.info("listContainer: id=" + container.getId() + " image=" + container.getImage());
        }

        assertThat(size + 1, is(equalTo(containers2.size())));
        Matcher matcher = hasItem(hasField("id", startsWith(container1.getId())));
        assertThat(containers2, matcher);

        List<Container> filteredContainers = filter(hasField("id", startsWith(container1.getId())), containers2);
        assertThat(filteredContainers.size(), is(equalTo(1)));

        for (Container container : filteredContainers) {
            LOG.info("filteredContainer: " + container);
        }

        Container container2 = filteredContainers.get(0);
        assertThat(container2.getCommand(), not(isEmptyString()));
        assertThat(container2.getImage(), startsWith(testImage));

        Map<String, String> labels = ImmutableMap.of("test", "docker-java");

        // list with filter by label
        dockerClient.createContainerCmd(testImage).withCmd("echo").withLabels(labels).exec();
        filteredContainers = dockerClient.listContainersCmd().withShowAll(true)
                .withLabelFilter(labels).exec();
        assertThat(filteredContainers.size(), is(equalTo(1)));
        Container container3 = filteredContainers.get(0);
        assertThat(container3.getCommand(), not(isEmptyString()));
        assertThat(container3.getImage(), startsWith(testImage));

        filteredContainers = dockerClient.listContainersCmd().withShowAll(true)
                .withLabelFilter("test").exec();
        assertThat(filteredContainers.size(), is(equalTo(1)));
        container3 = filteredContainers.get(0);
        assertThat(container3.getCommand(), not(isEmptyString()));
        assertThat(container3.getImage(), startsWith(testImage));
        assertEquals(container3.getLabels(), labels);
    }

}
