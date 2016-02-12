/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

/**
 *
 * @author Marcus Linke
 *
 */
public class PullImageResultCallback extends ResultCallbackTemplate<PullImageResultCallback, PullResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullImageResultCallback.class);

    @CheckForNull
    private PullResponseItem latestItem = null;

    @Override
    public void onNext(PullResponseItem item) {
        this.latestItem = item;
        LOGGER.debug(item.toString());
    }

    /**
     * Awaits the image to be pulled successful.
     *
     * @throws DockerClientException
     *             if the pull fails.
     */
    public void awaitSuccess() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }

        if (latestItem == null) {
            throw new DockerClientException("Could not pull image");
        } else if (!latestItem.isPullSuccessIndicated()) {
            String message = (latestItem.getError() != null) ? latestItem.getError() : latestItem.getStatus();
            throw new DockerClientException("Could not pull image: " + message);
        }
    }
}
