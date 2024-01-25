package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.HealthStateLog;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.core.RemoteApiVersion;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
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

        await().atMost(60L, TimeUnit.SECONDS).untilAsserted(
            () -> {
                InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
                assertThat(inspectContainerResponse.getState().getHealth().getStatus(), is(equalTo("healthy")));
            }
        );
    }

    @Test
    public void healthiness_startInterval() {
        assumeThat("API version should be >= 1.44", dockerRule, isGreaterOrEqual(RemoteApiVersion.VERSION_1_44));

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox")
            .withCmd("nc", "-l",  "-p", "8080")
            .withHealthcheck(new HealthCheck()
                .withTest(Arrays.asList("CMD", "sh", "-c", "netstat -ltn | grep 8080"))
                .withInterval(TimeUnit.SECONDS.toNanos(5))
                .withTimeout(TimeUnit.MINUTES.toNanos(1))
                .withStartPeriod(TimeUnit.SECONDS.toNanos(2))
                .withStartInterval(TimeUnit.SECONDS.toNanos(1))
                .withRetries(10))
            .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        await().atMost(60L, TimeUnit.SECONDS).untilAsserted(
            () -> {
                InspectContainerResponse inspectContainerResponse = dockerRule.getClient().inspectContainerCmd(container.getId()).exec();
                List<HealthStateLog> healthStateLogs = inspectContainerResponse.getState().getHealth().getLog();
                assertThat(healthStateLogs.size(), is(greaterThanOrEqualTo(2)));
                healthStateLogs.forEach(log -> LOG.info("Health log: {}", log.getStart()));
                HealthStateLog log1 = healthStateLogs.get(healthStateLogs.size() - 1);
                HealthStateLog log2 = healthStateLogs.get(healthStateLogs.size() - 2);
                long diff = ChronoUnit.NANOS.between(ZonedDateTime.parse(log2.getStart()), ZonedDateTime.parse(log1.getStart()));
                assertThat(diff, is(greaterThanOrEqualTo(inspectContainerResponse.getConfig().getHealthcheck().getInterval())));
            }
        );
    }

}
