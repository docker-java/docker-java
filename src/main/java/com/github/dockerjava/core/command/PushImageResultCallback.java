/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

/**
 *
 * @author Marcus Linke
 *
 */
public class PushImageResultCallback extends ResultCallbackTemplate<PushImageResultCallback, PushResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushImageResultCallback.class);

    @CheckForNull
    private PushResponseItem latestItem = null;

    @Override
    public void onNext(PushResponseItem item) {
        this.latestItem = item;
        LOGGER.debug(item.toString());
    }

    /**
     * Awaits the image to be pulled successful.
     *
     * @throws DockerClientException
     *             if the push fails.
     */
    public void awaitSuccess() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }

        if (latestItem == null) {
            throw new DockerClientException("Could not push image");
        } else if (latestItem.isErrorIndicated()) {
            throw new DockerClientException("Could not push image: " + latestItem.getError());
        }
    }
}
