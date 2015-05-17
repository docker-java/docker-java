package com.github.dockerjava.jaxrs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.jaxrs.util.Streaming;

public class EventsCmdExec extends AbstrDockerCmdExec<EventsCmd, Void>
		implements EventsCmd.Exec {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EventsCmdExec.class);

	public EventsCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected Void execute(EventsCmd command) {
		

		WebTarget webTarget = getBaseResource().path("/events")
				.queryParam("since", command.getSince())
				.queryParam("until", command.getUntil());

		LOGGER.trace("GET: {}", webTarget);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		EventNotifier eventNotifier = EventNotifier.create(
				command.getEventCallback(), webTarget);
		executorService.submit(eventNotifier);
		executorService.shutdown();

		return null;
	}

	private static class EventNotifier implements Callable<Void> {

		private final EventsCmd.EventStreamCallback eventCallback;
		private final WebTarget webTarget;

		private EventNotifier(EventsCmd.EventStreamCallback eventCallback,
				WebTarget webTarget) {
			this.eventCallback = eventCallback;
			this.webTarget = webTarget;
		}

		public static EventNotifier create(EventsCmd.EventStreamCallback eventCallback,
				WebTarget webTarget) {
			checkNotNull(eventCallback, "An EventCallback must be provided");
			checkNotNull(webTarget, "An WebTarget must be provided");
			return new EventNotifier(eventCallback, webTarget);
		}

		@Override
		public Void call() throws Exception {
			Response response = webTarget.request().get(Response.class);
						
			Streaming.processJsonStream(response, eventCallback, Event.class);
			
			return null;
		}
	}

	
}
