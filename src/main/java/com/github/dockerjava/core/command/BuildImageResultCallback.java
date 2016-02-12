/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import java.util.concurrent.TimeUnit;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

/**
 *
 * @author Marcus Linke
 *
 */
public class BuildImageResultCallback extends ResultCallbackTemplate<BuildImageResultCallback, BuildResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildImageResultCallback.class);

    @CheckForNull
    private BuildResponseItem latestItem = null;

    @Override
    public void onNext(BuildResponseItem item) {
        this.latestItem = item;
        LOGGER.debug(item.toString());
    }

    /**
     * Awaits the image id from the response stream.
     *
     * @throws DockerClientException
     *             if the build fails.
     */
    public String awaitImageId() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }

        return getImageId();
    }

    /**
     * Awaits the image id from the response stream.
     *
     * @throws DockerClientException
     *             if the build fails or the timeout occurs.
     */
    public String awaitImageId(long timeout, TimeUnit timeUnit) {
        try {
            awaitCompletion(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new DockerClientException("Awaiting image id interrupted: ", e);
        }

        return getImageId();
    }

    private String getImageId() {
        if (latestItem == null) {
            throw new DockerClientException("Could not build image");
        } else if (!latestItem.isBuildSuccessIndicated()) {
            throw new DockerClientException("Could not build image: " + latestItem.getError());
        } else {
            return latestItem.getImageId();
        }
    }

}
