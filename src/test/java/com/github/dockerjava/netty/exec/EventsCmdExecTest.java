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

    @Test
    public void testEventStreamTimeBound() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);

        String startTime = getEpochTime();
        int expectedEvents = generateEvents();
        String endTime = getEpochTime();

        EventsTestCallback eventCallback = new EventsTestCallback(expectedEvents);

        dockerClient.eventsCmd()
            .withSince(startTime)
            .withUntil(endTime)
            .exec(eventCallback);

        List<Event> events = eventCallback.awaitExpectedEvents(3, TimeUnit.MINUTES);

        // we may receive more events as expected
        assertTrue(events.size() >= expectedEvents, "Received events: " + events);
    }

    @Test
    public void testEventStreaming() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);
        
        String startTime = getEpochTime();
        int expectedEvents = generateEvents();

        EventsTestCallback eventCallback = new EventsTestCallback(expectedEvents);

        dockerClient.eventsCmd()
            .withSince(startTime)
            .exec(eventCallback);

        generateEvents();

        List<Event> events = eventCallback.awaitExpectedEvents(3, TimeUnit.MINUTES);
        
        // we may receive more events as expected
        assertTrue(events.size() >= expectedEvents, "Received events: " + events);
    }

    
    public void testEventStreamingWithFilter() throws Exception {
        // Don't include other tests events
        TimeUnit.SECONDS.sleep(1);
        
        String startTime = getEpochTime();
        int expectedEvents = 1;

        EventsTestCallback eventCallback = new EventsTestCallback(expectedEvents);
        
        dockerClient.eventsCmd()
        	.withSince(startTime)
        	.withEventFilter("start")
            .exec(eventCallback);

        generateEvents();

        List<Event> events = eventCallback.awaitExpectedEvents(3, TimeUnit.MINUTES);
        
        // we should get exactly one "start" event here
        assertEquals(events.size(), expectedEvents, "Received events: " + events);
    }

    /**
     * This method generates some events and returns the number of events being generated
     */
    private int generateEvents() throws Exception {
        String testImage = "busybox:latest";

        dockerClient.pullImageCmd(testImage).exec(new PullImageResultCallback()).awaitSuccess();
        CreateContainerResponse container = dockerClient.createContainerCmd(testImage).withCmd("sleep", "9999").exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        dockerClient.stopContainerCmd(container.getId()).withTimeout(1).exec();
        
        // generates 5 events with remote api 1.24:
        
        // Event[status=pull,id=busybox:latest,from=<null>,node=<null>,type=IMAGE,action=pull,actor=com.github.dockerjava.api.model.EventActor@417db6d7[id=busybox:latest,attributes={name=busybox}],time=1473455186,timeNano=1473455186436681587]
        // Event[status=create,id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,from=busybox:latest,node=<null>,type=CONTAINER,action=create,actor=com.github.dockerjava.api.model.EventActor@40bcec[id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,attributes={image=busybox:latest, name=sick_lamport}],time=1473455186,timeNano=1473455186470713257]
        // Event[status=<null>,id=<null>,from=<null>,node=<null>,type=NETWORK,action=connect,actor=com.github.dockerjava.api.model.EventActor@318a1b01[id=10870ceb13abb7cf841ea68868472da881b33c8ed08d2cde7dbb39d7c24d1d27,attributes={container=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c, name=bridge, type=bridge}],time=1473455186,timeNano=1473455186544318466]
        // Event[status=start,id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,from=busybox:latest,node=<null>,type=CONTAINER,action=start,actor=com.github.dockerjava.api.model.EventActor@606f43a3[id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,attributes={image=busybox:latest, name=sick_lamport}],time=1473455186,timeNano=1473455186786163819]
        // Event[status=kill,id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,from=busybox:latest,node=<null>,type=CONTAINER,action=kill,actor=com.github.dockerjava.api.model.EventActor@72a9ffcf[id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,attributes={image=busybox:latest, name=sick_lamport, signal=15}],time=1473455186,timeNano=1473455186792963392]

        return 5;
    }

    private class EventsTestCallback extends EventsResultCallback {

        private final CountDownLatch countDownLatch;

        private final List<Event> events = new ArrayList<Event>();

        public EventsTestCallback(int expextedEvents) {
            this.countDownLatch = new CountDownLatch(expextedEvents);
        }

        public void onNext(Event event) {
            LOG.info("Received event #{}: {}", countDownLatch.getCount(), event);
            events.add(event);
            countDownLatch.countDown();
        }
        
        public List<Event> awaitExpectedEvents(long timeout, TimeUnit unit ) {
        	try {
				countDownLatch.await(timeout, unit);
				close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
        	return new ArrayList<Event>(events);
        }
    }
}

