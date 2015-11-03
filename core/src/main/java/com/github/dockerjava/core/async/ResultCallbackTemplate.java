/*
 * Created on 16.06.2015
 */
package com.github.dockerjava.core.async;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;

/**
 * Abstract template implementation of {@link ResultCallback}
 *
 * @author marcus
 *
 */
public abstract class ResultCallbackTemplate<RC_T extends ResultCallback<A_RES_T>, A_RES_T> implements
        ResultCallback<A_RES_T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResultCallbackTemplate.class);

    private final CountDownLatch completed = new CountDownLatch(1);

    private Closeable stream;

    private boolean closed = false;

    @Override
    public void onStart(Closeable stream) {
        this.stream = stream;
        this.closed = false;
    }

    @Override
    public void onError(Throwable throwable) {
        if (closed)
            return;

        try {
            LOGGER.error("Error during callback", throwable);
            throw new RuntimeException(throwable);
        } finally {
            try {
                close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onComplete() {
        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
        if (stream != null)
            stream.close();
        completed.countDown();
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitCompletion() throws InterruptedException {
        completed.await();
        return (RC_T) this;
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called or the given timeout occurs
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        completed.await(timeout, timeUnit);
        return (RC_T) this;
    }
}
