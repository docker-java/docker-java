package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.RemoteApiVersion;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static com.github.dockerjava.utils.TestUtils.getVersion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;


public class CommitCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(CommitCmdIT.class);

    @Test
    public void commit() throws DockerException {
        assumeNotSwarm("", dockerRule);

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("touch", "/test")
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        LOG.info("Committing container: {}", container.toString());
        String imageId = dockerRule.getClient().commitCmd(container.getId()).exec();

        InspectImageResponse inspectImageResponse = dockerRule.getClient().inspectImageCmd(imageId).exec();
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        assertThat(inspectImageResponse, hasField("container", startsWith(container.getId())));
        assertThat(inspectImageResponse.getContainerConfig().getImage(), equalTo(DEFAULT_IMAGE));

        InspectImageResponse busyboxImg = dockerRule.getClient().inspectImageCmd("busybox").exec();

        assertThat(inspectImageResponse.getParent(), equalTo(busyboxImg.getId()));
    }

    @Test
    public void commitWithLabels() throws DockerException {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
                .withCmd("touch", "/test")
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        LOG.info("Committing container: {}", container.toString());
        Map<String, String> labels = ImmutableMap.of("label1", "abc", "label2", "123");
        String imageId = dockerRule.getClient().commitCmd(container.getId())
                .withLabels(labels)
                .exec();

        InspectImageResponse inspectImageResponse = dockerRule.getClient().inspectImageCmd(imageId).exec();
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        // docker bug?
        if (!getVersion(dockerRule.getClient()).isGreaterOrEqual(RemoteApiVersion.VERSION_1_24)) {
            Map<String, String> responseLabels = inspectImageResponse.getContainerConfig().getLabels();
            assertThat(responseLabels.size(), is(2));
            assertThat(responseLabels.get("label1"), equalTo("abc"));
            assertThat(responseLabels.get("label2"), equalTo("123"));
        }
    }

    @Test(expected = NotFoundException.class)
    public void commitNonExistingContainer() throws DockerException {

        dockerRule.getClient().commitCmd("non-existent").exec();
    }
}
