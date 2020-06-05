/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Marcus Linke
 *
 * @deprecated use {@link com.github.dockerjava.api.command.PullImageResultCallback}
 */
@Deprecated
public class PullImageResultCallback extends ResultCallbackTemplate<PullImageResultCallback, PullResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullImageResultCallback.class);

    private boolean isSwarm = false;
    private Map<String, PullResponseItem> results = null;

    @CheckForNull
    private PullResponseItem latestItem = null;

    @Override
    public void onNext(PullResponseItem item) {
        // only do it once
        if (results == null && latestItem == null) {
            checkForDockerSwarmResponse(item);
        }

        if (isSwarm) {
            handleDockerSwarmResponse(item);
        } else {
            handleDockerClientResponse(item);
        }

        LOGGER.debug(item.toString());
    }

    private void checkForDockerSwarmResponse(PullResponseItem item) {
        if (item.getStatus().matches("Pulling\\s.+\\.{3}$")) {
            isSwarm = true;
            LOGGER.debug("Communicating with Docker Swarm.");
        }
    }

    private void handleDockerSwarmResponse(final PullResponseItem item) {
        if (results == null) {
            results = new HashMap<>();
        }

        // Swarm terminates a pull sometimes with an empty line.
        // Therefore keep first success message
        PullResponseItem currentItem = results.get(item.getId());
        if (currentItem == null || !currentItem.isPullSuccessIndicated()) {
            results.put(item.getId(), item);
        }
    }

    private void handleDockerClientResponse(PullResponseItem item) {
        latestItem = item;
    }

    private void checkDockerSwarmPullSuccessful() {
        if (results.isEmpty()) {
            throw new DockerClientException("Could not pull image through Docker Swarm");
        } else {
            boolean pullFailed = false;
            StringBuilder sb = new StringBuilder();

            for (PullResponseItem pullResponseItem : results.values()) {
                if (!pullResponseItem.isPullSuccessIndicated()) {
                    pullFailed = true;
                    sb.append("[" + pullResponseItem.getId() + ":" + messageFromPullResult(pullResponseItem) + "]");
                }
            }

            if (pullFailed) {
                throw new DockerClientException("Could not pull image: " + sb.toString());
            }
        }
    }

    private void checkDockerClientPullSuccessful() {
        if (latestItem == null) {
            throw new DockerClientException("Could not pull image");
        } else if (!latestItem.isPullSuccessIndicated()) {
            throw new DockerClientException("Could not pull image: " + messageFromPullResult(latestItem));
        }
    }

    private String messageFromPullResult(PullResponseItem pullResponseItem) {
        return (pullResponseItem.getError() != null) ? pullResponseItem.getError() : pullResponseItem.getStatus();
    }

    @Override
    protected void throwFirstError() {
        super.throwFirstError();

        if (isSwarm) {
            checkDockerSwarmPullSuccessful();
        } else {
            checkDockerClientPullSuccessful();
        }
    }

    /**
     * Awaits the image to be pulled successful.
     *
     * @deprecated use {@link #awaitCompletion()} or {@link #awaitCompletion(long, TimeUnit)} instead
     * @throws DockerClientException
     *             if the pull fails.
     */
    public void awaitSuccess() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }
    }
}
