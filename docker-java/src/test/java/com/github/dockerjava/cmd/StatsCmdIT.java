package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertTrue;

public class StatsCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(StatsCmdIT.class);

    private static int NUM_STATS = 5;

    @Test
    public void testStatsStreaming() throws InterruptedException, IOException {
        TimeUnit.SECONDS.sleep(1);

        CountDownLatch countDownLatch = new CountDownLatch(NUM_STATS);

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        StatsCallbackTest statsCallback = dockerRule.getClient().statsCmd(container.getId()).exec(
                new StatsCallbackTest(countDownLatch));

        countDownLatch.await(3, TimeUnit.SECONDS);
        Boolean gotStats = statsCallback.gotStats();

        LOG.info("Stop stats collection");

        statsCallback.close();

        LOG.info("Stopping container");
        dockerRule.getClient().stopContainerCmd(container.getId()).exec();
        dockerRule.getClient().removeContainerCmd(container.getId()).exec();

        LOG.info("Completed test");
        assertTrue("Expected true", gotStats);

    }

    private class StatsCallbackTest extends ResultCallbackTemplate<StatsCallbackTest, Statistics> {
        private final CountDownLatch countDownLatch;

        private Boolean gotStats = false;

        public StatsCallbackTest(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onNext(Statistics stats) {
            LOG.info("Received stats #{}: {}", countDownLatch.getCount(), stats);
            if (stats != null) {
                gotStats = true;
            }
            countDownLatch.countDown();
        }

        public Boolean gotStats() {
            return gotStats;
        }
    }
}
