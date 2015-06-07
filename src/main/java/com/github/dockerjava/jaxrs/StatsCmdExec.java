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
import com.github.dockerjava.api.command.StatsCallback;
import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;

public class StatsCmdExec extends AbstrDockerCmdExec<StatsCmd, ExecutorService> implements StatsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsCmdExec.class);

    public StatsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected ExecutorService execute(StatsCmd command) {
    	ExecutorService executorService = Executors.newSingleThreadExecutor();

        WebTarget webResource = getBaseResource().path("/containers/{id}/stats")
            .resolveTemplate("id", command.getContainerId());

        LOGGER.trace("GET: {}", webResource);
        StatsNotifier eventNotifier = StatsNotifier.create(command.getStatsCallback(), webResource);
        executorService.submit(eventNotifier);
        return executorService;
    }

    private static class StatsNotifier implements Callable<Void> {
        private static final JsonFactory JSON_FACTORY = new JsonFactory();
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

        private final StatsCallback statsCallback;
        private final WebTarget webTarget;

        private StatsNotifier(StatsCallback statsCallback, WebTarget webTarget) {
            this.statsCallback = statsCallback;
            this.webTarget = webTarget;
        }

        public static StatsNotifier create(StatsCallback statsCallback, WebTarget webTarget) {
            checkNotNull(statsCallback, "An StatsCallback must be provided");
            checkNotNull(webTarget, "An WebTarget must be provided");
            return new StatsNotifier(statsCallback, webTarget);
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
                while (statsCallback.isReceiving() &&
                       jp.nextToken() != JsonToken.END_OBJECT && !jp.isClosed() &&
                               statsCallback.isReceiving()) {
                    try {
                        statsCallback.onStats(OBJECT_MAPPER.readValue(jp,
                                Statistics.class));
                    } catch (Exception e) {
                        statsCallback.onException(e);
                    }
                    numEvents++;
                }
            } catch (Exception e) {
                statsCallback.onException(e);
            } finally {
                if (response != null) {
                    response.close();
                }
                try {
                    statsCallback.onCompletion(numEvents);
                } catch (Exception e) {
                    statsCallback.onException(e);
                }
            }

            return null;
        }
    }
}
