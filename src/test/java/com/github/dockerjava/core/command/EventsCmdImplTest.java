package com.github.dockerjava.core.command;

import java.io.Closeable;
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
import com.github.dockerjava.api.command.EventsCmd.EventStreamCallback;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
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
	public void testEventStreamTimeBound() throws InterruptedException,
			IOException {
		// Don't include other tests events
		TimeUnit.SECONDS.sleep(1);

		String startTime = getEpochTime();
		int expectedEvents = generateEvents();
		String endTime = getEpochTime();

		CountDownLatch countDownLatch = new CountDownLatch(expectedEvents);
		EventCallbackTest eventCallback = new EventCallbackTest(countDownLatch);

		dockerClient.eventsCmd(eventCallback)
				.withSince(startTime)
				.withUntil(endTime)
				.exec();
		
		boolean zeroCount = countDownLatch.await(5, TimeUnit.SECONDS);
		
		eventCallback.close();
		
		LOG.debug("events: " + eventCallback.getEvents());

		assertTrue(zeroCount, "" + eventCallback.getEvents());
	}

	@Test
	public void testEventStreamingUnbound() throws InterruptedException, IOException {
		// Don't include other tests events
		TimeUnit.SECONDS.sleep(1);

		CountDownLatch countDownLatch = new CountDownLatch(KNOWN_NUM_EVENTS);
		EventCallbackTest eventCallback = new EventCallbackTest(countDownLatch);

		dockerClient.eventsCmd(eventCallback)
			.withSince(getEpochTime())
			.exec();

		generateEvents();

		boolean zeroCount = countDownLatch.await(5, TimeUnit.SECONDS);

		System.out.println("close callback");
		
		eventCallback.close();
		assertTrue(zeroCount, "Expected 4 events, [create, start, die, stop]");
		
		assertTrue(eventCallback.getErrors().isEmpty(), "At least on Exception was thrown: " + eventCallback.getErrors());
	}

	@Test
	public void testEventStreamingUnbound2() throws InterruptedException, IOException {
		// Don't include other tests events
		TimeUnit.SECONDS.sleep(1);

		CountDownLatch countDownLatch = new CountDownLatch(KNOWN_NUM_EVENTS);
		EventCallbackTest eventCallback = new EventCallbackTest(countDownLatch);

		dockerClient.eventsCmd(eventCallback)
			.withSince(getEpochTime())
			.exec();

		generateEvents();

		boolean zeroCount = countDownLatch.await(5, TimeUnit.SECONDS);

		eventCallback.close();
		assertTrue(zeroCount, "Expected 4 events, [create, start, die, stop]");
	}

	/**
	 * This method generates {#link KNOWN_NUM_EVENTS} events
	 */
	private int generateEvents() {
		String testImage = "busybox";
		asString(dockerClient.pullImageCmd(testImage).exec());
		CreateContainerResponse container = dockerClient
				.createContainerCmd(testImage).withCmd("sleep", "9999").exec();
		dockerClient.startContainerCmd(container.getId()).exec();
		dockerClient.stopContainerCmd(container.getId()).exec();
		return KNOWN_NUM_EVENTS;
	}

	private class EventCallbackTest implements EventStreamCallback, Closeable {
		private final CountDownLatch countDownLatch;
		private final List<Event> events = new ArrayList<Event>();
		private final List<Throwable> errors = new ArrayList<Throwable>();
		private Closeable closeable;

		public EventCallbackTest(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void close() throws IOException {
			closeable.close();
		}
		
		@Override
		public void streamStarted(Closeable closeable) {
			this.closeable = closeable;
		}

		@Override
		public void onStream(Event event) {
			LOG.info("Received event #{}: {}", countDownLatch.getCount(), event);
			countDownLatch.countDown();
			events.add(event);
		}

		@Override
		public void onError(Throwable throwable) {
			LOG.error("Error occurred: {}", throwable.getMessage());
			errors.add(throwable);
		}
		
		@Override
		public void streamFinished() {
			LOG.info("Event stream finished");
		}

		public List<Event> getEvents() {
			return new ArrayList<Event>(events);
		}
		
		public List<Throwable> getErrors() {
			return errors;
		}
	}
}
