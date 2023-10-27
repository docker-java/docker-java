package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallbackTemplate;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.LoadResponseItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadImageCallback extends ResultCallbackTemplate<LoadImageCallback, LoadResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadImageCallback.class);

    private String message;

    private String error;

    @Override
    public void onNext(LoadResponseItem item) {
        if (item.isBuildSuccessIndicated()) {
            this.message = item.getMessage();
        } else if (item.isErrorIndicated()) {
            this.error = item.getError();
        }

        LOGGER.debug("{}", item);
    }

    public String awaitMessage() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }

        return getMessage();
    }

    private String getMessage() {
        if (this.message != null) {
            return this.message;
        }

        if (this.error == null) {
            throw new DockerClientException("Could not build image");
        }

        throw new DockerClientException("Could not build image: " + this.error);
    }
}
