package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.LoadImageResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;

/**
 * @author Kanstantsin Shautsou
 */
public class LoadImageCallback extends ResultCallbackTemplate<LoadImageCallback, LoadImageResponseItem> {
    private static final Logger LOG = LoggerFactory.getLogger(LoadImageCallback.class);

    @CheckForNull
    protected LoadImageResponseItem latestItem = null;

    @Override
    public void onNext(LoadImageResponseItem responseItem) {
        this.latestItem = responseItem;
        LOG.debug(responseItem.toString());
    }

    public void awaitSuccess() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("Interrupted", e);
        }

        if (latestItem == null) {
            throw new DockerClientException("Could not load image");
        } else if (latestItem.isErrorIndicated()) {
            throw new DockerClientException("Could not load image: " + latestItem.getErrorDetail());
        }
    }
}
