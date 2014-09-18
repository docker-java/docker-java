package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.api.model.EventStream;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.google.common.collect.Lists;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EventsCmdImplTest extends AbstractDockerClientTest {

    private static int KNOWN_NUM_EVENTS = 4;

    private static String getEpochTime() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    @BeforeTest
    public void beforeTest() throws DockerException {
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

    @Test
    public void testEventStreamTimeBound() throws InterruptedException, IOException {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);
        String startTime = getEpochTime();
        generateEvents();
        String endTime = getEpochTime();

        EventStream eventStream = dockerClient.eventsCmd().withSince(startTime).withUntil(endTime).exec();
        List<Event> eventList = pollEvents(eventStream);
        eventStream.close();
        LOG.info("Events: {}", eventList);
        assertEquals(eventList.size(), KNOWN_NUM_EVENTS, "Expected 4 events, [create, start, die, stop]");
    }

    @Test
    public void testEventStream() throws InterruptedException, IOException {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);
        String startTime = getEpochTime();
        generateEvents();

        EventStream eventStream = dockerClient.eventsCmd().withSince(startTime).exec();
        List<Event> eventList = pollEvents(eventStream);
        eventStream.close();
        LOG.info("Events: {}", eventList);
        assertEquals(eventList.size(), KNOWN_NUM_EVENTS, "Expected 4 events, [create, start, die, stop]");
    }

    /**
     * This method generates {#link KNOWN_NUM_EVENTS} events
     */
    private void generateEvents() {
        String testImage = "busybox";
        asString(dockerClient.pullImageCmd(testImage).exec());
        CreateContainerResponse container1 = dockerClient
                .createContainerCmd(testImage).withCmd("echo").exec();
        dockerClient.startContainerCmd(container1.getId()).exec();
        dockerClient.stopContainerCmd(container1.getId()).exec();
    }

    private List<Event> pollEvents(EventStream eventStream) throws InterruptedException {
        List<Event> eventList = Lists.newArrayList();
        Event event = null;
        do {
            event = eventStream.pollEvent(1, TimeUnit.SECONDS);
            if (event != null) {
                eventList.add(event);
            }
        } while (event != null);
        return eventList;
    }
}
