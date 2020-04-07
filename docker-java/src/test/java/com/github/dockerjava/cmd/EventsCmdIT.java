package com.github.dockerjava.cmd;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.utils.TestUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.github.dockerjava.junit.DockerAssume.assumeNotSwarm;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

/*
 * NOTE: These tests may fail if there is a difference between local and daemon time
 * (this is especially a problem when using boot2docker as time may not in sync
 * with the virtualbox host system)
 */
public class EventsCmdIT extends CmdIT {
    private static final Logger LOG = LoggerFactory.getLogger(EventsCmdIT.class);

    private static String getEpochTime() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    @Test
    public void testEventStreamTimeBound() throws Exception {
        //since until and filtering events is broken in swarm
        //https://github.com/docker/swarm/issues/1203
        assumeNotSwarm("", dockerRule);

        String startTime = getEpochTime();
        int expectedEvents = generateEvents();
        String endTime = getEpochTime();

        EventsTestCallback eventCallback = new EventsTestCallback(expectedEvents);

        dockerRule.getClient().eventsCmd()
                .withSince(startTime)
                .withUntil(endTime)
                .exec(eventCallback);

        List<Event> events = eventCallback.awaitExpectedEvents(30, TimeUnit.SECONDS);

        // we may receive more events as expected
        assertTrue("Received events: " + events, events.size() >= expectedEvents);
    }

    @Test
    public void testEventStreaming() throws Exception {
        String startTime = getEpochTime();

        int expectedEvents = generateEvents();

        EventsTestCallback eventCallback = new EventsTestCallback(expectedEvents);

        dockerRule.getClient().eventsCmd()
                .withSince(startTime)
                .exec(eventCallback);

        generateEvents();

        List<Event> events = eventCallback.awaitExpectedEvents(30, TimeUnit.SECONDS);

        // we may receive more events as expected
        assertTrue("Received events: " + events, events.size() >= expectedEvents);

        for (Event event : events) {
            if (TestUtils.isSwarm(dockerRule.getClient())) {
                assertThat(event.getNode(), is(notNullValue()));
                assertThat(event.getNode().getAddr(), is(notNullValue()));
                assertThat(event.getNode().getId(), is(notNullValue()));
                assertThat(event.getNode().getIp(), is(notNullValue()));
                assertThat(event.getNode().getName(), is(notNullValue()));
            } else {
                assertThat(event.getNode(), is(nullValue()));
            }
        }
    }

    @Test
    public void testEventStreamingWithFilter() throws Exception {
        //since until and filtering events is broken in swarm
        //https://github.com/docker/swarm/issues/1203
        assumeNotSwarm("", dockerRule);

        String startTime = getEpochTime();
        int expectedEvents = 1;

        EventsTestCallback eventCallback = new EventsTestCallback(expectedEvents);

        dockerRule.getClient().eventsCmd()
                .withSince(startTime)
                .withEventFilter("start")
                .exec(eventCallback);

        generateEvents();

        List<Event> events = eventCallback.awaitExpectedEvents(30, TimeUnit.SECONDS);

        // we should only get "start" events here
        for (Event event : events) {
            assertThat("Received event: " + event, event.getAction(), is("start"));
        }
    }

    /**
     * This method generates some events and returns the number of events being generated
     */
    private int generateEvents() throws Exception {
        String testImage = "busybox:latest";

        dockerRule.getClient().pullImageCmd(testImage).start().awaitCompletion();
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(testImage).withCmd("sleep", "9999").exec();
        dockerRule.getClient().startContainerCmd(container.getId()).exec();
        dockerRule.getClient().stopContainerCmd(container.getId()).withTimeout(1).exec();

        // generates 5 events with remote api 1.24:

        // Event[status=pull,id=busybox:latest,from=<null>,node=<null>,type=IMAGE,action=pull,actor=com.github.dockerjava.api.model.EventActor@417db6d7[id=busybox:latest,attributes={name=busybox}],time=1473455186,timeNano=1473455186436681587]
        // Event[status=create,id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,from=busybox:latest,node=<null>,type=CONTAINER,action=create,actor=com.github.dockerjava.api.model.EventActor@40bcec[id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,attributes={image=busybox:latest, name=sick_lamport}],time=1473455186,timeNano=1473455186470713257]
        // Event[status=<null>,id=<null>,from=<null>,node=<null>,type=NETWORK,action=connect,actor=com.github.dockerjava.api.model.EventActor@318a1b01[id=10870ceb13abb7cf841ea68868472da881b33c8ed08d2cde7dbb39d7c24d1d27,attributes={container=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c, name=bridge, type=bridge}],time=1473455186,timeNano=1473455186544318466]
        // Event[status=start,id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,from=busybox:latest,node=<null>,type=CONTAINER,action=start,actor=com.github.dockerjava.api.model.EventActor@606f43a3[id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,attributes={image=busybox:latest, name=sick_lamport}],time=1473455186,timeNano=1473455186786163819]
        // Event[status=kill,id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,from=busybox:latest,node=<null>,type=CONTAINER,action=kill,actor=com.github.dockerjava.api.model.EventActor@72a9ffcf[id=6ec10182cde227040bfead8547b63105e6bbc4e94b99f6098bfad6e158ce0d3c,attributes={image=busybox:latest, name=sick_lamport, signal=15}],time=1473455186,timeNano=1473455186792963392]

        return 5;
    }

    private class EventsTestCallback extends ResultCallback.Adapter<Event> {

        private final CountDownLatch countDownLatch;

        private final List<Event> events = new ArrayList<>();

        public EventsTestCallback(int expextedEvents) {
            this.countDownLatch = new CountDownLatch(expextedEvents);
        }

        public void onNext(Event event) {
            LOG.info("Received event #{}: {}", countDownLatch.getCount(), event);
            events.add(event);
            countDownLatch.countDown();
        }

        public List<Event> awaitExpectedEvents(long timeout, TimeUnit unit) {
            try {
                countDownLatch.await(timeout, unit);
                close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new ArrayList<>(events);
        }
    }
}
