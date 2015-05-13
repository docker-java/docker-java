package com.github.dockerjava.jaxrs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.EventCallback;
import com.github.dockerjava.api.command.EventsCmd;
import com.github.dockerjava.api.model.Event;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;

public class EventsCmdExec extends
		AbstrDockerCmdExec<EventsCmd, ExecutorService> implements
		EventsCmd.Exec {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EventsCmdExec.class);

	public EventsCmdExec(WebTarget baseResource) {
		super(baseResource);
	}

	@Override
	protected ExecutorService execute(EventsCmd command) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		WebTarget webResource = getBaseResource().path("/events")
				.queryParam("since", command.getSince())
				.queryParam("until", command.getUntil());

		LOGGER.trace("GET: {}", webResource);
		EventNotifier eventNotifier = EventNotifier.create(
				command.getEventCallback(), webResource);
		executorService.submit(eventNotifier);
		return executorService;
	}

	private static class EventNotifier implements Callable<Void> {
		private static final JsonFactory JSON_FACTORY = new JsonFactory();
		private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

		private final EventCallback eventCallback;
		private final WebTarget webTarget;

		private EventNotifier(EventCallback eventCallback, WebTarget webTarget) {
			this.eventCallback = eventCallback;
			this.webTarget = webTarget;
		}

		public static EventNotifier create(EventCallback eventCallback,
				WebTarget webTarget) {
			checkNotNull(eventCallback, "An EventCallback must be provided");
			checkNotNull(webTarget, "An WebTarget must be provided");
			return new EventNotifier(eventCallback, webTarget);
		}

		@Override
		public Void call() throws Exception {
			int numEvents = 0;
			Response response = null;
			try {
				response = webTarget.request().get(Response.class);
				InputStream inputStream = new WrappedResponseInputStream(
						response);
				JsonParser jp = JSON_FACTORY.createParser(inputStream);
				// The following condition looks strange but jp.nextToken() will block until there is an
				// event from the docker server or the connection is terminated.
				// therefore we want to check before getting an event (to prevent a blocking operation
				// and after the event to make sure that the eventCallback is still interested in getting notified.
				while (eventCallback.isReceiving() && 
				       jp.nextToken() != JsonToken.END_OBJECT && !jp.isClosed() &&
				       eventCallback.isReceiving()) {
					try {
						eventCallback.onEvent(OBJECT_MAPPER.readValue(jp,
								Event.class));
					} catch (Exception e) {
						eventCallback.onException(e);
					}
					numEvents++;
				}
			} catch (Exception e) {
				eventCallback.onException(e);
			} finally {
				// call onCompletion before close because of https://github.com/docker-java/docker-java/issues/196
				try {
					eventCallback.onCompletion(numEvents);
				} catch (Exception e) {
					eventCallback.onException(e);
				}
				if (response != null) {
					response.close();
				}
			}

			return null;
		}
	}
}
