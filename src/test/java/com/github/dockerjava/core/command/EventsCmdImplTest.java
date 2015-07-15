package com.github.dockerjava.core.command;

import java.io.IOException;
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

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

@Test(groups = "integration")
public class EventsCmdImplTest extends AbstractDockerClientTest {

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
        EventCallbackTest eventCallback = new EventCallbackTest(countDownLatch);

        dockerClient.eventsCmd().withSince(startTime).withUntil(endTime).exec(eventCallback);

        boolean zeroCount = countDownLatch.await(10, TimeUnit.SECONDS);

        eventCallback.close();

        assertTrue(zeroCount, "Received only: " + eventCallback.getEvents());
    }

    @Test
    public void testEventStreaming1() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);

        CountDownLatch countDownLatch = new CountDownLatch(KNOWN_NUM_EVENTS);
        EventCallbackTest eventCallback = new EventCallbackTest(countDownLatch);

        dockerClient.eventsCmd().withSince(getEpochTime()).exec(eventCallback);

        generateEvents();

        boolean zeroCount = countDownLatch.await(10, TimeUnit.SECONDS);

        eventCallback.close();
        assertTrue(zeroCount, "Received only: " + eventCallback.getEvents());
    }

    @Test
    public void testEventStreaming2() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);

        CountDownLatch countDownLatch = new CountDownLatch(KNOWN_NUM_EVENTS);
        EventCallbackTest eventCallback = new EventCallbackTest(countDownLatch);

        dockerClient.eventsCmd().withSince(getEpochTime()).exec(eventCallback);

        generateEvents();

        boolean zeroCount = countDownLatch.await(10, TimeUnit.SECONDS);

        eventCallback.close();
        assertTrue(zeroCount, "Received only: " + eventCallback.getEvents());
    }

    /**
     * This method generates {#link KNOWN_NUM_EVENTS} events
     */
    private int generateEvents() throws Exception {
        String testImage = "busybox";

        dockerClient.pullImageCmd(testImage).exec(new PullResponseCallback()).awaitCompletion();

        CreateContainerResponse container = dockerClient.createContainerCmd(testImage).withCmd("sleep", "9999").exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.stopContainerCmd(container.getId()).exec();
        return KNOWN_NUM_EVENTS;
    }

    private class EventCallbackTest extends ResultCallbackTemplate<EventCallbackTest, Event> {

        private final CountDownLatch countDownLatch;

        private final List<Event> events = new ArrayList<Event>();

        public EventCallbackTest(CountDownLatch countDownLatch) {
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
