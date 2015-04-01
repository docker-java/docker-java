package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.EventCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.client.AbstractDockerClientTest;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

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

		EventsCmd eventsCmd = dockerClient.eventsCmd(eventCallback)
				.withSince(startTime).withUntil(endTime);
		ExecutorService executorService = eventsCmd.exec();

		boolean zeroCount = countDownLatch.await(5, TimeUnit.SECONDS);

		executorService.shutdown();
		eventCallback.close();

		assertTrue(zeroCount, "Expected 4 events, [create, start, die, stop]");
	}

	@Test
	public void testEventStreaming() throws InterruptedException, IOException {
		// Don't include other tests events
		TimeUnit.SECONDS.sleep(1);

		CountDownLatch countDownLatch = new CountDownLatch(KNOWN_NUM_EVENTS);
		EventCallbackTest eventCallback = new EventCallbackTest(countDownLatch);

		EventsCmd eventsCmd = dockerClient.eventsCmd(eventCallback).withSince(
				getEpochTime());
		ExecutorService executorService = eventsCmd.exec();

		generateEvents();

		boolean zeroCount = countDownLatch.await(5, TimeUnit.SECONDS);
		executorService.shutdown();
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

	private class EventCallbackTest implements EventCallback {
		private final CountDownLatch countDownLatch;
		private final AtomicBoolean isReceiving = new AtomicBoolean(true);

		public EventCallbackTest(CountDownLatch countDownLatch) {
			this.countDownLatch = countDownLatch;
		}

		public void close() {
			isReceiving.set(false);
		}

		@Override
		public void onEvent(Event event) {
			LOG.info("Received event #{}: {}", countDownLatch.getCount(), event);
			countDownLatch.countDown();
		}

		@Override
		public void onException(Throwable throwable) {
			LOG.error("Error occurred: {}", throwable.getMessage());
		}

		@Override
		public void onCompletion(int numEvents) {
			LOG.info("Number of events received: {}", numEvents);
		}

		@Override
		public boolean isReceiving() {
			return isReceiving.get();
		}
	}
}
