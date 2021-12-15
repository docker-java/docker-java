package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.junit.PrivateRegistryRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.utils.TestUtils.getVersion;
import static com.github.dockerjava.utils.TestUtils.isNotSwarm;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;

public class PushImageCmdIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(PushImageCmdIT.class);

    @ClassRule
    public static PrivateRegistryRule REGISTRY = new PrivateRegistryRule();

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private AuthConfig authConfig;

    @Before
    public void beforeTest() throws Exception {
        authConfig = REGISTRY.getAuthConfig();
    }

    @Test
    public void pushLatest() throws Exception {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("true").exec();

        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        LOG.info("Committing container: {}", container.toString());
        String imgName = authConfig.getRegistryAddress() + "/" + dockerRule.getKind() + "-push-latest";
        String imageId = dockerRule.getClient().commitCmd(container.getId())
                .withRepository(imgName)
                .exec();

        // we have to block until image is pushed
        dockerRule.getClient().pushImageCmd(imgName)
                .withAuthConfig(authConfig)
                .start()
                .awaitCompletion(30, TimeUnit.SECONDS);

        LOG.info("Removing image: {}", imageId);
        dockerRule.getClient().removeImageCmd(imageId).exec();

        dockerRule.getClient().pullImageCmd(imgName)
                .withTag("latest")
                .withAuthConfig(authConfig)
                .start()
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

        dockerRule.getClient().pushImageCmd(UUID.randomUUID().toString().replace("-", ""))
                .start()
                .awaitCompletion(30, TimeUnit.SECONDS); // exclude infinite await sleep

    }

    @Test
    public void testPushImageWithValidAuth() throws Exception {
        String imgName = REGISTRY.createTestImage("push-image-with-valid-auth");

        // stream needs to be fully read in order to close the underlying connection
        dockerRule.getClient().pushImageCmd(imgName)
                .withAuthConfig(authConfig)
                .start()
                .awaitCompletion(30, TimeUnit.SECONDS);
    }

    @Test
    public void testPushImageWithNoAuth() throws Exception {
        String imgName = REGISTRY.createTestImage("push-image-with-no-auth");

        exception.expect(DockerClientException.class);

        // stream needs to be fully read in order to close the underlying connection
        dockerRule.getClient().pushImageCmd(imgName)
                .start()
                .awaitCompletion(30, TimeUnit.SECONDS);
    }

    @Test
    public void testPushImageWithInvalidAuth() throws Exception {
        AuthConfig invalidAuthConfig = new AuthConfig()
                .withUsername("testuser")
                .withPassword("testwrongpassword")
                .withEmail("foo@bar.de")
                .withRegistryAddress(authConfig.getRegistryAddress());

        String imgName = REGISTRY.createTestImage("push-image-with-invalid-auth");

        exception.expect(DockerClientException.class);

        // stream needs to be fully read in order to close the underlying connection
        dockerRule.getClient().pushImageCmd(imgName)
                .withAuthConfig(invalidAuthConfig)
                .start()
                .awaitCompletion(30, TimeUnit.SECONDS);
    }
}
