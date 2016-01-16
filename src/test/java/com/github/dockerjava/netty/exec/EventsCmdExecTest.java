package com.github.dockerjava.netty.exec;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.core.command.EventsResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class EventsCmdExecTest extends AbstractNettyDockerClientTest {

    private static int KNOWN_NUM_EVENTS = 4;

    private static String getEpochTime() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

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

    /*
     * This specific test may fail with boot2docker as time may not in sync with host system
     */
    @Test
    public void testEventStreamTimeBound() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);

        String startTime = getEpochTime();
        int expectedEvents = generateEvents();
        String endTime = getEpochTime();

        CountDownLatch countDownLatch = new CountDownLatch(expectedEvents);
        EventsTestCallback eventCallback = new EventsTestCallback(countDownLatch);

        dockerClient.eventsCmd().withSince(startTime).withUntil(endTime).exec(eventCallback);

        Boolean zeroCount = countDownLatch.await(10, TimeUnit.SECONDS);

        eventCallback.close();

        assertTrue(zeroCount, "Received only: " + eventCallback.getEvents());
    }

    @Test
    public void testEventStreaming1() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);

        CountDownLatch countDownLatch = new CountDownLatch(KNOWN_NUM_EVENTS);
        EventsTestCallback eventCallback = new EventsTestCallback(countDownLatch);

        dockerClient.eventsCmd().withSince(getEpochTime()).exec(eventCallback);

        generateEvents();

        Boolean zeroCount = countDownLatch.await(10, TimeUnit.SECONDS);

        eventCallback.close();
        assertTrue(zeroCount, "Received only: " + eventCallback.getEvents());
    }

    @Test
    public void testEventStreaming2() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);

        CountDownLatch countDownLatch = new CountDownLatch(KNOWN_NUM_EVENTS);
        EventsTestCallback eventCallback = new EventsTestCallback(countDownLatch);

        dockerClient.eventsCmd().withSince(getEpochTime()).exec(eventCallback);

        generateEvents();

        Boolean zeroCount = countDownLatch.await(10, TimeUnit.SECONDS);

        eventCallback.close();
        assertTrue(zeroCount, "Received only: " + eventCallback.getEvents());
    }

    public void testEventStreamingWithFilter() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);

        CountDownLatch countDownLatch = new CountDownLatch(1);
        EventsTestCallback eventCallback = dockerClient.eventsCmd().withEventFilter("start")
                .exec(new EventsTestCallback(countDownLatch));

        generateEvents();

        Boolean zeroCount = countDownLatch.await(10, TimeUnit.SECONDS);

        eventCallback.close();
        assertTrue(zeroCount, "Received only: " + eventCallback.getEvents());
    }

    /**
     * This method generates {#link KNOWN_NUM_EVENTS} events
     */
    private int generateEvents() throws Exception {
        String testImage = "busybox";

        dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitSuccess();

        CreateContainerResponse container = dockerClient.createContainerCmd(testImage).withCmd("sleep", "9999").exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.stopContainerCmd(container.getId()).exec();
        return KNOWN_NUM_EVENTS;
    }

    private class EventsTestCallback extends EventsResultCallback {

        private final CountDownLatch countDownLatch;

        private final List<Event> events = new ArrayList<Event>();

        public EventsTestCallback(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        public void onNext(Event event) {
            LOG.info("Received event #{}: {}", countDownLatch.getCount(), event);
            countDownLatch.countDown();
            events.add(event);
        }

        public List<Event> getEvents() {
            return new ArrayList<Event>(events);
        }
    }
}
