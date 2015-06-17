package com.github.dockerjava.jaxrs;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.command.StatsCmd.StatisticsCallback;
import com.github.dockerjava.api.model.Statistics;
import com.github.dockerjava.jaxrs.util.Streaming;

public class StatsCmdExec extends AbstrDockerCmdExec<StatsCmd, Void> implements StatsCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsCmdExec.class);

    public StatsCmdExec(WebTarget baseResource) {
        super(baseResource);
    }

    @Override
    protected Void execute(StatsCmd command) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        WebTarget webResource = getBaseResource().path("/containers/{id}/stats").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("GET: {}", webResource);
        StatsNotifier eventNotifier = StatsNotifier.create(command.getStatisticsCallback(), webResource);
        executorService.submit(eventNotifier);
        executorService.shutdown();

        return null;
    }

    private static class StatsNotifier implements Callable<Void> {
        private final StatisticsCallback statisticsCallback;
        private final WebTarget webTarget;

        private StatsNotifier(StatisticsCallback statisticsCallback, WebTarget webTarget) {
            this.statisticsCallback = statisticsCallback;
            this.webTarget = webTarget;
        }

        public static StatsNotifier create(StatisticsCallback statisticsCallback, WebTarget webTarget) {
            checkNotNull(statisticsCallback, "An StatsCallback must be provided");
            checkNotNull(webTarget, "An WebTarget must be provided");
            return new StatsNotifier(statisticsCallback, webTarget);
        }

        @Override
        public Void call() throws Exception {
            Response response = webTarget.request().get(Response.class);

            Streaming.processJsonStream(response, statisticsCallback, Statistics.class);

            return null;
        }
    }
}
