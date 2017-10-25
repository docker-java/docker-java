package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.dockerjava.junit.category.AuthIntegration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

public class PushImageCmdIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(PushImageCmdIT.class);

    private String username;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void beforeTest() throws Exception {
        username = dockerRule.getClient().authConfig().getUsername();
    }

    @Category(AuthIntegration.class)
    @Test
    public void pushLatest() throws Exception {

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true").exec();

        LOG.info("Created container {}", container.toString());

        assertThat(container.getId(), not(isEmptyString()));

        LOG.info("Committing container: {}", container.toString());
        String imageId = dockerRule.getClient().commitCmd(container.getId()).withRepository(username + "/busybox").exec();

        // we have to block until image is pushed
        dockerRule.getClient().pushImageCmd(username + "/busybox").exec(new PushImageResultCallback())
                .awaitCompletion(30, TimeUnit.SECONDS);

        LOG.info("Removing image: {}", imageId);
        dockerRule.getClient().removeImageCmd(imageId).exec();

        dockerRule.getClient().pullImageCmd(username + "/busybox").exec(new PullImageResultCallback())
                .awaitCompletion(30, TimeUnit.SECONDS);
    }

    @Test
    public void pushNonExistentImage() throws Exception {
        //swarms throws a different error here
        try {
            dockerRule.getClient().pushImageCmd(username + "/xxx")
                    .exec(new PushImageResultCallback())
                    .awaitCompletion(30, TimeUnit.SECONDS);

            Assert.fail("An exception is expected.");
        } catch (DockerClientException | NotFoundException e) {
            // expected exception
        }
    }
}
