package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

public class PushImageCmdTest extends CmdTest {

    public static final Logger LOG = LoggerFactory.getLogger(PushImageCmdTest.class);

    String username;

    @Before
    public void beforeTest() throws Exception {
        username = dockerRule.getClient().authConfig().getUsername();
    }
    
    @Test
    public void pushLatest() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        LOG.info("Committing container: {}", container.toString());
        String imageId = dockerRule.getClient().commitCmd(container.getId()).withRepository(username + "/busybox").exec();

        // we have to block until image is pushed
        dockerRule.getClient().pushImageCmd(username + "/busybox").exec(new PushImageResultCallback()).awaitSuccess();

        LOG.info("Removing image: {}", imageId);
        dockerRule.getClient().removeImageCmd(imageId).exec();

        dockerRule.getClient().pullImageCmd(username + "/busybox").exec(new PullImageResultCallback()).awaitSuccess();
    }

    @Test(expected = DockerClientException.class)
    public void pushNonExistentImage() throws Exception {

        dockerRule.getClient().pushImageCmd(username + "/xxx").exec(new PushImageResultCallback()).awaitSuccess();

    }

}
