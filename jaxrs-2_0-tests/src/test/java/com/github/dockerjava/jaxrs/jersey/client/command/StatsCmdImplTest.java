package com.github.dockerjava.jaxrs.jersey.client.command;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.github.dockerjava.jaxrs.jersey.client.client.AbstractDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

@Test(groups = "integration")
public class StatsCmdImplTest extends AbstractDockerClientTest {

    private static int NUM_STATS = 5;

    @BeforeTest
    public void beforeTest() throws Exception {
        super.beforeTest();
    }

    @AfterTest
    public void afterTest() {
        super.afterTest();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        super.afterMethod(result);
    }

    @Test(groups = "ignoreInCircleCi")
    public void testStatsStreaming() throws InterruptedException, IOException {
        TimeUnit.SECONDS.sleep(1);

        CountDownLatch countDownLatch = new CountDownLatch(NUM_STATS);

        String containerName = "generated_" + new SecureRandom().nextInt();

        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("top")
                .withName(containerName).exec();
        AbstractDockerClientTest.LOG.info("Created container {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        StatsCallbackTest statsCallback = dockerClient.statsCmd().withContainerId(container.getId())
                .exec(new StatsCallbackTest(countDownLatch));

        countDownLatch.await(3, TimeUnit.SECONDS);
        boolean gotStats = statsCallback.gotStats();

        AbstractDockerClientTest.LOG.info("Stop stats collection");

        statsCallback.close();

        AbstractDockerClientTest.LOG.info("Stopping container");
        dockerClient.stopContainerCmd(container.getId()).exec();
        dockerClient.removeContainerCmd(container.getId()).exec();

        AbstractDockerClientTest.LOG.info("Completed test");
        assertTrue(gotStats, "Expected true");

    }

    private class StatsCallbackTest extends ResultCallbackTemplate<StatsCallbackTest, Statistics> {
        private final CountDownLatch countDownLatch;

        private boolean gotStats = false;

        public StatsCallbackTest(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onNext(Statistics stats) {
            AbstractDockerClientTest.LOG.info("Received stats #{}: {}", countDownLatch.getCount(), stats);
            if (stats != null) {
                gotStats = true;
            }
            countDownLatch.countDown();
        }

        public boolean gotStats() {
            return gotStats;
        }
    }
}
