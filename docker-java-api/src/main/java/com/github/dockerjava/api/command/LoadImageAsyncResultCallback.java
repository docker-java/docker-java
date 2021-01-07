package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.LoadImageAsyncResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luwt
 * @date 2021/1/7.
 */
public class LoadImageAsyncResultCallback extends ResultCallbackTemplate<LoadImageAsyncResultCallback, LoadImageAsyncResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageAsyncResultCallback.class);

    private String imageId;

    private String error;


    @Override
    public void onNext(LoadImageAsyncResponseItem item) {
        if (item.isLoadSuccessIndicated()) {
            this.imageId = item.getImageId();
        } else if (item.isErrorIndicated()) {
            this.error = item.getError();
        }
        LOGGER.debug(item.toString());
    }

    /**
     * Awaits the image id from the response stream.
     *
     * @throws DockerClientException
     *             if the load fails.
     */
    public String awaitImageId() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }

        return getImageId();
    }

    private String getImageId() {
        if (imageId != null) {
            return imageId;
        }

        if (error == null) {
            throw new DockerClientException("Could not load image");
        }

        throw new DockerClientException("Could not load image: " + error);
    }
}
