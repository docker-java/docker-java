package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.core.RemoteApiVersion;
import com.google.common.collect.ImmutableMap;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.filter;
import static com.github.dockerjava.utils.TestUtils.getVersion;
import static com.github.dockerjava.utils.TestUtils.isNotSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.testinfected.hamcrest.jpa.PersistenceMatchers.hasField;

public class ListContainersCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(ListContainersCmdIT.class);

    @Test
    public void testListContainers() throws Exception {
        Map<String, String> testLabel = Collections.singletonMap("test", "testListContainers");

        final RemoteApiVersion apiVersion = getVersion(dockerRule.getClient());
        String testImage = "busybox";

        List<Container> containers = dockerRule.getClient().listContainersCmd()
                .withLabelFilter(testLabel)
                .withShowAll(true)
                .exec();
        assertThat(containers, notNullValue());
        LOG.info("Container List: {}", containers);

        int size = containers.size();

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(testImage)
                .withLabels(testLabel)
                .withCmd("echo")
                .exec();
        assertThat(container1.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container1.getId()).exec();
        assertThat(inspectContainerResponse.getConfig().getImage(), is(equalTo(testImage)));

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
        assertThat(container2.getCommand(), not(isEmptyString()));
        assertThat(container2.getImage(), startsWith(testImage));

        if (apiVersion.isGreaterOrEqual(RemoteApiVersion.VERSION_1_23)) {
            assertThat(container2.getState(), equalTo("running"));
        }
    }

    @Test
    public void testListContainersWithLabelsFilter() throws Exception {

        String testImage = "busybox";

        List<Container> containers = dockerRule.getClient().listContainersCmd().withShowAll(true).exec();
        assertThat(containers, notNullValue());
        LOG.info("Container List: {}", containers);

//        int size = containers.size();

        CreateContainerResponse container1 = dockerRule.getClient().createContainerCmd(testImage).withCmd("echo").exec();

        assertThat(container1.getId(), not(isEmptyString()));

        InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container1.getId()).exec();

        assertThat(inspectContainerResponse.getConfig().getImage(), is(equalTo(testImage)));

        dockerRule.getClient().startContainerCmd(container1.getId()).exec();

        LOG.info("container id: " + container1.getId());

        List<Container> containers2 = dockerRule.getClient().listContainersCmd().withShowAll(true).exec();

        for (Container container : containers2) {
            LOG.info("listContainer: id=" + container.getId() + " image=" + container.getImage());
        }

        // some parallel test may create containers
//        assertThat(size + 1, is(equalTo(containers2.size())));
        assertThat(containers2, (Matcher) hasItem(hasField("id", startsWith(container1.getId()))));

        List<Container> filteredContainers = filter(hasField("id", startsWith(container1.getId())), containers2);
        assertThat(filteredContainers.size(), is(equalTo(1)));

        for (Container container : filteredContainers) {
            LOG.info("filteredContainer: " + container);
        }

        Container container2 = filteredContainers.get(0);
        assertThat(container2.getCommand(), not(isEmptyString()));
        assertThat(container2.getImage(), startsWith(testImage));



        Map<String, String> labels = ImmutableMap.of("test" + dockerRule.getKind(), "docker-java");
        List<Container> containersForRemove = dockerRule.getClient().listContainersCmd().withShowAll(true)
                .withLabelFilter(labels).exec();
        if (containersForRemove != null) {
            for (Container container : containersForRemove) {
                dockerRule.getClient().removeContainerCmd(container.getId()).withForce(true).exec();
            }
        }

        // list with filter by Map label
        dockerRule.getClient().createContainerCmd(testImage).withCmd("echo").withLabels(labels).exec();

        List<Container> filteredContainersByMap = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter(labels)
                .exec();

        assertThat(filteredContainersByMap.size(), is(equalTo( 1)));

        Container container3 = filteredContainersByMap.get(0);
        assertThat(container3.getCommand(), not(isEmptyString()));
        assertThat(container3.getImage(), startsWith(testImage));

        // List by string label
        filteredContainers = dockerRule.getClient().listContainersCmd()
                .withShowAll(true)
                .withLabelFilter("test" + dockerRule.getKind())
                .exec();

        assertThat(filteredContainers.size(), is(equalTo(1)));

        container3 = filteredContainers.get(0);
        assertThat(container3.getCommand(), not(isEmptyString()));
        assertThat(container3.getImage(), startsWith(testImage));
        if (isNotSwarm(dockerRule.getClient())) {
            assertEquals(container3.getLabels(), labels);
        }
    }

}
