/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

import javax.annotation.CheckForNull;

/**
 *
 * @author marcus
 *
 */
public class PushImageResultCallback extends ResultCallbackTemplate<PushImageResultCallback, PushResponseItem> {

    private final static Logger LOGGER = LoggerFactory.getLogger(PushImageResultCallback.class);

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
