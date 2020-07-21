package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectImageResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;


public class CommitCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(CommitCmdIT.class);

    @Test
    public void commit() throws DockerException, InterruptedException {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("touch", "/test")
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        LOG.info("Committing container: {}", container.toString());
        String imageId = dockerRule.getClient().commitCmd(container.getId()).exec();

        //swarm needs some time to reflect new images
        synchronized (this) {
            wait(5000);
        }

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
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        Integer status = dockerRule.getClient().waitContainerCmd(container.getId())
                .start()
                .awaitStatusCode();

        assertThat(status, is(0));

        LOG.info("Committing container: {}", container.toString());
        Map<String, String> labels = ImmutableMap.of("label1", "abc", "label2", "123");
        String imageId = dockerRule.getClient().commitCmd(container.getId())
                .withLabels(labels)
                .exec();

        InspectImageResponse inspectImageResponse = dockerRule.getClient().inspectImageCmd(imageId).exec();
        LOG.info("Image Inspect: {}", inspectImageResponse.toString());

        //use config here since containerConfig contains the configuration of the container which was
        //committed to the container
        //https://stackoverflow.com/questions/36216220/what-is-different-of-config-and-containerconfig-of-docker-inspect
        Map<String, String> responseLabels = inspectImageResponse.getConfig().getLabels();
        //swarm will attach additional labels here
        assertThat(responseLabels.size(), greaterThanOrEqualTo(2));
        assertThat(responseLabels.get("label1"), equalTo("abc"));
        assertThat(responseLabels.get("label2"), equalTo("123"));
    }

    @Test(expected = NotFoundException.class)
    public void commitNonExistingContainer() throws DockerException {

        dockerRule.getClient().commitCmd("non-existent").exec();
    }
}
