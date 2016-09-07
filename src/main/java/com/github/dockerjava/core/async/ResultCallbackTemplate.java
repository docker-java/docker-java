/*
 * Created on 16.06.2015
 */
package com.github.dockerjava.core.async;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.google.common.base.Throwables;

/**
 * Abstract template implementation of {@link ResultCallback}
 *
 * @author Marcus Linke
 *
 */
public abstract class ResultCallbackTemplate<RC_T extends ResultCallback<A_RES_T>, A_RES_T> implements
        ResultCallback<A_RES_T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultCallbackTemplate.class);

    private final CountDownLatch started = new CountDownLatch(1);

    private final CountDownLatch completed = new CountDownLatch(1);

    private Closeable stream;

    private boolean closed = false;

    private Throwable firstError = null;

    @Override
    public void onStart(Closeable stream) {
        this.stream = stream;
        this.closed = false;
        started.countDown();
    }

    @Override
    public void onError(Throwable throwable) {

        if (closed) return;

        if (this.firstError == null) {
            this.firstError = throwable;
        }

        try {
            LOGGER.error("Error during callback", throwable);
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
        if (!closed) {
           closed = true;
           if (stream != null) {
               stream.close();
           }
           completed.countDown();
        }
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitCompletion() throws InterruptedException {
        completed.await();
        // eventually (re)throws RuntimeException
        getFirstError();
        return (RC_T) this;
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called or the given timeout occurs
     * @return {@code true} if completed and {@code false} if the waiting time elapsed
     *         before {@link ResultCallback#onComplete()} was called.
     */
    public boolean awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return completed.await(timeout, timeUnit);
    }

    /**
     * Blocks until {@link ResultCallback#onStart()} was called. {@link ResultCallback#onStart()} is called when the request was processed
     * on the server side and the response is incoming.
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitStarted() throws InterruptedException {
        started.await();
        return (RC_T) this;
    }

    /**
     * Blocks until {@link ResultCallback#onStart()} was called or the given timeout occurs. {@link ResultCallback#onStart()} is called when
     * the request was processed on the server side and the response is incoming.
     * @return {@code true} if started and {@code false} if the waiting time elapsed
     *         before {@link ResultCallback#onStart()} was called.
     */
    public boolean awaitStarted(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return started.await(timeout, timeUnit);
    }

    @CheckForNull
    protected RuntimeException getFirstError() {
        if (firstError != null) {
            // this call throws a RuntimeException
            return Throwables.propagate(firstError);
        } else {
            return null;
        }
    }
}
