package com.github.dockerjava.cmd;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Statistics;

public class SyncStatsCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(SyncStatsCmdIT.class);

    @Test
    public void testStats() throws InterruptedException, IOException {
        TimeUnit.SECONDS.sleep(1);

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        Statistics statistics = dockerRule.getClient().syncStatsCmd(container.getId()).exec();
        assertThat(statistics.getCpuStats().getCpuUsage().getTotalUsage(), is(greaterThan(0L)));
        assertThat(statistics.getMemoryStats().getUsage(), is(greaterThan(0L)));
        
        LOG.info("Stopping container");
        dockerRule.getClient().stopContainerCmd(container.getId()).exec();
        dockerRule.getClient().removeContainerCmd(container.getId()).exec();
        LOG.info("Completed test");
    }
}
