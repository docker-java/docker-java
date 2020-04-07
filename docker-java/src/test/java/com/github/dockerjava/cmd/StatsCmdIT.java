package com.github.dockerjava.cmd;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Statistics;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StatsCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(StatsCmdIT.class);

    private static int NUM_STATS = 3;

    @Test
    public void testStatsStreaming() throws InterruptedException, IOException {
        CountDownLatch countDownLatch = new CountDownLatch(NUM_STATS);

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top").exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        boolean gotStats = false;
        try (StatsCallbackTest statsCallback = dockerRule.getClient()
            .statsCmd(container.getId())
            .exec(new StatsCallbackTest(countDownLatch))) {

            assertTrue(countDownLatch.await(10, TimeUnit.SECONDS));
            gotStats = statsCallback.gotStats();

            LOG.info("Stop stats collection");
        }

        LOG.info("Stopping container");
        dockerRule.getClient().stopContainerCmd(container.getId()).exec();
        dockerRule.getClient().removeContainerCmd(container.getId()).exec();

        LOG.info("Completed test");
        assertTrue("Expected true", gotStats);
    }

    @Test
    public void testStatsNoStreaming() throws InterruptedException, IOException {
        CountDownLatch countDownLatch = new CountDownLatch(NUM_STATS);

        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("top").exec();

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        try (StatsCallbackTest statsCallback = dockerRule.getClient().statsCmd(container.getId())
            .withNoStream(true)
            .exec(new StatsCallbackTest(countDownLatch))) {
            countDownLatch.await(5, TimeUnit.SECONDS);

            LOG.info("Stop stats collection");
        }

        LOG.info("Stopping container");
        dockerRule.getClient().stopContainerCmd(container.getId()).exec();
        dockerRule.getClient().removeContainerCmd(container.getId()).exec();

        LOG.info("Completed test");
        assertEquals("Expected stats called only once", countDownLatch.getCount(), NUM_STATS - 1);
    }

    private static class StatsCallbackTest extends ResultCallbackTemplate<StatsCallbackTest, Statistics> {
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
