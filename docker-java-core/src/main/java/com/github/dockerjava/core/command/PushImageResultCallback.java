/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.PushResponseItem;
import com.github.dockerjava.core.async.ResultCallbackTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Marcus Linke
 *
 * @deprecated use {@link com.github.dockerjava.api.async.ResultCallback.Adapter}
 */
@Deprecated
public class PushImageResultCallback extends ResultCallbackTemplate<PushImageResultCallback, PushResponseItem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PushImageResultCallback.class);

    @CheckForNull
    private PushResponseItem latestItem = null;

    @Override
    public void onNext(PushResponseItem item) {
        this.latestItem = item;
        LOGGER.debug(item.toString());
    }

    @Override
    protected void throwFirstError() {
        super.throwFirstError();

        if (latestItem == null) {
            throw new DockerClientException("Could not push image");
        } else if (latestItem.isErrorIndicated()) {
            throw new DockerClientException("Could not push image: " + latestItem.getError());
        }
    }

    /**
     * Awaits the image to be pulled successful.
     *
     * @deprecated use {@link #awaitCompletion()} or {@link #awaitCompletion(long, TimeUnit)} instead
     * @throws DockerClientException
     *             if the push fails.
     */
    public void awaitSuccess() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }
    }
}
