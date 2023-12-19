package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.HealthState;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.core.RemoteApiVersion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

public class HealthCmdIT extends CmdIT {
    private final Logger LOG = LoggerFactory.getLogger(HealthCmdIT.class);

    @Test
    public void healthiness() {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
            .withCmd("nc", "-l",  "-p", "8080")
            .withHealthcheck(new HealthCheck()
                .withTest(Arrays.asList("CMD", "sh", "-c", "netstat -ltn | grep 8080"))
                .withInterval(TimeUnit.SECONDS.toNanos(1))
                .withTimeout(TimeUnit.MINUTES.toNanos(1))
                .withStartPeriod(TimeUnit.SECONDS.toNanos(30))
                .withRetries(10))
            .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        HealthState healthState = await().pollInterval(Duration.ofSeconds(5)).atMost(60L, TimeUnit.SECONDS).until(
            () -> {
                InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
                return inspectContainerResponse.getState().getHealth();
            },
            Objects::nonNull
        );

        assertThat(healthState.getStatus(), is(equalTo("healthy")));
    }

    @Test
    public void healthiness_startInterval() {
        assumeThat("API version should be >= 1.44", dockerRule, isGreaterOrEqual(RemoteApiVersion.VERSION_1_44));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
            .withCmd("nc", "-l",  "-p", "8080")
            .withHealthcheck(new HealthCheck()
                .withTest(Arrays.asList("CMD", "sh", "-c", "netstat -ltn | grep 8080"))
                .withInterval(TimeUnit.SECONDS.toNanos(1))
                .withTimeout(TimeUnit.MINUTES.toNanos(1))
                .withStartPeriod(TimeUnit.SECONDS.toNanos(30))
                .withStartInterval(TimeUnit.SECONDS.toNanos(5))
                .withRetries(10))
            .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        HealthState healthState = await().atMost(60L, TimeUnit.SECONDS).until(
            () -> {
                InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
                return inspectContainerResponse.getState().getHealth();
            },
            Objects::nonNull
        );

        assertThat(healthState.getStatus(), is(equalTo("healthy")));
    }

}
