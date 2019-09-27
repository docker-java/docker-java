/*
 * Created on 21.07.2015
 */
package com.github.dockerjava.core.command;

import java.util.concurrent.TimeUnit;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.WaitResponse;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

/**
 *
 * @author Marcus Linke
 *
 */
public class WaitContainerResultCallback extends ResultCallbackTemplate<WaitContainerResultCallback, WaitResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitContainerResultCallback.class);

    @CheckForNull
    private WaitResponse waitResponse = null;

    @Override
    public void onNext(WaitResponse waitResponse) {
        this.waitResponse = waitResponse;
        LOGGER.debug(waitResponse.toString());
    }

    /**
     * Awaits the status code from the container.
     *
     * @throws DockerClientException
     *             if the wait operation fails.
     */
    public Integer awaitStatusCode() {
        try {
            awaitCompletion();
        } catch (InterruptedException e) {
            throw new DockerClientException("", e);
        }

        return getStatusCode();
    }

    /**
     * Awaits the status code from the container.
     *
     * @throws DockerClientException
     *             if the wait operation fails.
     */
    public Integer awaitStatusCode(long timeout, TimeUnit timeUnit) {
        try {
            if (!awaitCompletion(timeout, timeUnit)) {
                throw new DockerClientException("Awaiting status code timeout.");
            }
        } catch (InterruptedException e) {
            throw new DockerClientException("Awaiting status code interrupted: ", e);
        }

        return getStatusCode();
    }

    private Integer getStatusCode() {
        if (waitResponse == null) {
            throw new DockerClientException("Error while wait container");
        } else {
            return waitResponse.getStatusCode();
        }
    }
}
