/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.model.PullResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

import javax.annotation.CheckForNull;

/**
 *
 * @author marcus
 *
 */
public class PullImageResultCallback extends ResultCallbackTemplate<PullImageResultCallback, PullResponseItem> {

    private final static Logger LOGGER = LoggerFactory.getLogger(PullImageResultCallback.class);

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
            throw new DockerClientException("Could not pull image: " + latestItem.getError());
        }
    }
}
