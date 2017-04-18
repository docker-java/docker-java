/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import com.google.common.collect.EvictingQueue;

/**
 *
 * @author Marcus Linke
 *
 */
public class BuildImageResultCallback extends ResultCallbackTemplate<BuildImageResultCallback, BuildResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuildImageResultCallback.class);

    //  Keep last few items for more detailed analysis
    private EvictingQueue<BuildResponseItem> latestItems = EvictingQueue.create(2);

    @Override
    public void onNext(BuildResponseItem item) {
        latestItems.add(item);
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
        BuildResponseItem buildSuccessItem = findBuildSuccessItem();

        if (buildSuccessItem == null) {
            BuildResponseItem errorItem = findErrorItem();
            if (errorItem == null) {
                throw new DockerClientException("Could not build image");
            } else {
                throw new DockerClientException("Could not build image: " + errorItem.getError());
            }
        }

        return buildSuccessItem.getImageId();
    }

    private BuildResponseItem findBuildSuccessItem() {
        for (BuildResponseItem item : latestItems) {
            if (item.isBuildSuccessIndicated()) {
                return item;
            }
        }

        return null;
    }

    private BuildResponseItem findErrorItem() {
        for (BuildResponseItem item : latestItems) {
            if (item.isErrorIndicated()) {
                return item;
            }
        }

        return null;
    }
}
