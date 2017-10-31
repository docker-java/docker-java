package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.PushImageResultCallback;
import com.github.dockerjava.junit.category.AuthIntegration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static com.github.dockerjava.utils.TestUtils.isNotSwarm;
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

        if (isNotSwarm(dockerRule.getClient()) && getVersion(dockerRule.getClient())
                .isGreaterOrEqual(RemoteApiVersion.VERSION_1_24)) {
            exception.expect(DockerClientException.class);
        } else {
            exception.expect(NotFoundException.class);
        }

        dockerRule.getClient().pushImageCmd(username + "/xxx")
                .exec(new PushImageResultCallback())
                .awaitCompletion(30, TimeUnit.SECONDS); // exclude infinite await sleep

    }
}
