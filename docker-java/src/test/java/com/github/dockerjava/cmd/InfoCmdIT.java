package com.github.dockerjava.cmd;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Info;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static com.github.dockerjava.utils.TestUtils.isNotSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class InfoCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(InfoCmdIT.class);

    @Test
    public void infoTest() throws DockerException {
        DockerClient dockerClient = dockerRule.getClient();
        // Make sure that there is at least one container for the assertion
        // TODO extract this into a shared method
        if (dockerClient.listContainersCmd().withShowAll(true).exec().size() == 0) {
            CreateContainerResponse container = dockerClient.createContainerCmd(DEFAULT_IMAGE)
                    .withName("docker-java-itest-info")
                    .withCmd("touch", "/test")
                    .exec();

            LOG.info("Created container: {}", container);
            assertThat(container.getId(), not(isEmptyOrNullString()));

            dockerClient.startContainerCmd(container.getId()).exec();
        }

        Info dockerInfo = dockerClient.infoCmd().exec();
        LOG.info(dockerInfo.toString());

        assertThat(dockerInfo.getContainers(), notNullValue());
        assertThat(dockerInfo.getContainers(), greaterThan(0));

        assertThat(dockerInfo.getImages(), notNullValue());
        assertThat(dockerInfo.getImages(), greaterThan(0));
        assertThat(dockerInfo.getDebug(), notNullValue());

        if (isNotSwarm(dockerClient)) {
            assertThat(dockerInfo.getNFd(), greaterThan(0));
            assertThat(dockerInfo.getNGoroutines(), greaterThan(0));
            assertThat(dockerInfo.getNCPU(), greaterThan(0));
        }
    }
}
