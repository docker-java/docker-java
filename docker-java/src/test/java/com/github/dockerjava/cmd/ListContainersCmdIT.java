package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.junit.DockerAssume;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ch.lambdaj.Lambda.filter;
import static com.github.dockerjava.api.model.HostConfig.newHostConfig;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.testinfected.hamcrest.jpa.PersistenceMatchers.hasField;

public class ListContainersCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ListContainersCmdIT.class);
    private static final String DEFAULT_IMAGE = "busybox";
    private Map<String, String> testLabel;

    @Before
    public void setUp() {
        //generate unique ids per test to isolate each test case from each other
        testLabel = Collections.singletonMap("test", UUID.randomUUID().toString());
    }

    @After
    public void tearDown() {
        //remove all containers created by this test
        List<Container> containers = dockerRule.getClient().listContainersCmd()
                .withLabelFilter(testLabel)
                .withShowAll(true)
                .exec();

        for (Container container : containers) {
            removeContainer(container.getId());
        }
    }

    @Test
    public void testListContainers() throws Exception {
        List<Container> containers = dockerRule.getClient().listContainersCmd()
                .withLabelFilter(testLabel)
                .withShowAll(true)
                .exec();
        assertThat(containers, notNullValue());
        LOG.info("Container List: {}", containers);

        int size = containers.size();

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .withCmd("echo")
                .exec();
        assertThat(container1.getId(), not(is(emptyString())));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container1.getId()).exec();
        assertThat(inspectContainerResponse.getConfig().getImage(), is(equalTo(DEFAULT_IMAGE)));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();
        LOG.info("container id: " + container1.getId());

        List<Container> containers2 = dockerRule.getClient().listContainersCmd()
                .withLabelFilter(testLabel)
                .withShowAll(true)
                .exec();
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
        assertThat(container2.getCommand(), not(is(emptyString())));
        assertThat(container2.getImage(), startsWith(DEFAULT_IMAGE));
    }

    @Test
    public void testListContainersWithLabelsFilter() throws Exception {
        // list with filter by Map label
        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE).withCmd("echo")
                .withLabels(testLabel)
                .exec();

        List<Container> filteredContainersByMap = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(testLabel)
                .exec();

        assertThat(filteredContainersByMap.size(), is(1));

        Container container3 = filteredContainersByMap.get(0);
        assertThat(container3.getCommand(), not(is(emptyString())));
        assertThat(container3.getImage(), startsWith(DEFAULT_IMAGE));

        // List by string label
        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(singletonList("test=" + testLabel.get("test")))
                .exec();

        assertThat(filteredContainers.size(), is(1));

        container3 = filteredContainers.get(0);
        assertThat(container3.getCommand(), not(is(emptyString())));
        assertThat(container3.getImage(), startsWith(DEFAULT_IMAGE));
        assertEquals(testLabel.get("test"), container3.getLabels().get("test"));
    }

    @Test
    public void testNameFilter() throws Exception {
        String testUUID = testLabel.get("test");

        String id1, id2;
        id1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .withName("nameFilterTest1-" + testUUID)
                .exec()
                .getId();

        id2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .withName("nameFilterTest2-" + testUUID)
                .exec()
                .getId();

        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withNameFilter(asList("nameFilterTest1-" + testUUID, "nameFilterTest2-" + testUUID))
                .exec();

        assertThat(filteredContainers.size(), is(2));
        assertThat(filteredContainers.get(0).getId(), isOneOf(id1, id2));
        assertThat(filteredContainers.get(1).getId(), isOneOf(id1, id2));
    }

    @Test
    public void testIdsFilter() throws Exception {
        String id1, id2;
        id1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .exec()
                .getId();

        id2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .exec()
                .getId();

        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withIdFilter(asList(id1, id2))
                .exec();

        assertThat(filteredContainers.size(), is(2));
        assertThat(filteredContainers.get(0).getId(), isOneOf(id1, id2));
        assertThat(filteredContainers.get(1).getId(), isOneOf(id1, id2));
    }

    @Test
    public void testStatusFilter() throws Exception {
        String id1, id2;
        id1 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sh", "-c", "sleep 99999")
                .withLabels(testLabel)
                .exec()
                .getId();

        id2 = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sh", "-c", "sleep 99999")
                .withLabels(testLabel)
                .exec()
                .getId();

        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(testLabel)
                .withStatusFilter(singletonList("created"))
                .exec();

        assertThat(filteredContainers.size(), is(2));
        assertThat(filteredContainers.get(1).getId(), isOneOf(id1, id2));

        dockerRule.getClient().startContainerCmd(id1).exec();

        filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(testLabel)
                .withStatusFilter(singletonList("running"))
                .exec();

        assertThat(filteredContainers.size(), is(1));
        assertThat(filteredContainers.get(0).getId(), is(id1));

        dockerRule.getClient().pauseContainerCmd(id1).exec();

        filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(testLabel)
                .withStatusFilter(singletonList("paused"))
                .exec();

        assertThat(filteredContainers.size(), is(1));
        assertThat(filteredContainers.get(0).getId(), is(id1));

        dockerRule.getClient().unpauseContainerCmd(id1).exec();
        dockerRule.getClient().stopContainerCmd(id1).exec();

        filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(testLabel)
                .withStatusFilter(singletonList("exited"))
                .exec();

        assertThat(filteredContainers.size(), is(1));
        assertThat(filteredContainers.get(0).getId(), is(id1));
    }

    @Test
    public void testVolumeFilter() throws Exception {
        String id;
        dockerRule.getClient().createVolumeCmd()
                .withName("TestFilterVolume")
                .withDriver("local")
                .exec();

        id = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .withHostConfig(newHostConfig()
                        .withBinds(new Bind("TestFilterVolume", new Volume("/test"))))
                .exec()
                .getId();

        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .exec();

        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(testLabel)
                .withVolumeFilter(singletonList("TestFilterVolume"))
                .exec();

        assertThat(filteredContainers.size(), is(1));
        assertThat(filteredContainers.get(0).getId(), is(id));
    }

    @Test
    public void testNetworkFilter() throws Exception {
        String id;
        dockerRule.getClient().createNetworkCmd()
                .withName("TestFilterNetwork")
                .withDriver("bridge")
                .exec();

        id = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .withHostConfig(newHostConfig()
                        .withNetworkMode("TestFilterNetwork"))
                .exec()
                .getId();

        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .exec();

        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(testLabel)
                .withNetworkFilter(singletonList("TestFilterNetwork"))
                .exec();

        assertThat(filteredContainers.size(), is(1));
        assertThat(filteredContainers.get(0).getId(), is(id));
    }

    @Test
    public void testAncestorFilter() throws Exception {
        // ancestor filters are broken in swarm
        // https://github.com/docker/swarm/issues/1716
        DockerAssume.assumeNotSwarm(dockerRule.getClient());

        dockerRule.getClient().pullImageCmd("busybox")
                .withTag("1.24")
                .start()
                .awaitCompletion();

        dockerRule.getClient().createContainerCmd("busybox:1.24")
                .withLabels(testLabel)
                .exec();

        String imageId = dockerRule.getClient().inspectImageCmd(DEFAULT_IMAGE).exec().getId();

        String id = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .exec()
                .getId();

        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withLabelFilter(testLabel)
                .withShowAll(true)
                .withAncestorFilter(singletonList(imageId))
                .exec();

        assertThat(filteredContainers.size(), is(1));
        assertThat(filteredContainers.get(0).getId(), is(id));
    }

    @Test
    public void testExitedFilter() throws Exception {
        dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .exec();

        String id = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withLabels(testLabel)
                .withCmd("sh", "-c", "exit 42")
                .exec()
                .getId();

        dockerRule.getClient().startContainerCmd(id).exec();

        Integer status = dockerRule.getClient().waitContainerCmd(id).start()
                .awaitStatusCode();

        assertThat(status, is(42));

        List<Container> filteredContainers = dockerRule.getClient().listContainersCmd()
                .withLabelFilter(testLabel)
                .withShowAll(true)
                .withExitedFilter(42)
                .exec();

        assertThat(filteredContainers.size(), is(1));
        assertThat(filteredContainers.get(0).getId(), is(id));
    }

    private void removeContainer(String id) {
        if (id != null) {
            try {
                dockerRule.getClient().removeContainerCmd(id).withForce(true).exec();
            } catch (Exception ignored) {}
        }
    }
}
