/*
 * Created on 16.06.2015
 */
package com.github.dockerjava.core.async;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.github.dockerjava.api.async.ResultCallback;

/**
 * Abstract template implementation of {@link ResultCallback}
 *
 * @author marcus
 *
 */
public abstract class ResultCallbackTemplate<RC_T extends ResultCallback<A_RES_T>, A_RES_T> implements ResultCallback<A_RES_T> {

    private final CountDownLatch finished = new CountDownLatch(1);

    private Closeable stream;

    @Override
    public void onStart(Closeable stream) {
        this.stream = stream;
    }

    @Override
    public void onNext(A_RES_T object) {
    }

    @Override
    public void onError(Throwable throwable) {
        try {
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
        if (stream != null)
            stream.close();
        finished.countDown();
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitCompletion() throws InterruptedException {
        finished.await();
        return (RC_T) this;
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called or the given timeout occurs
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        finished.await(timeout, timeUnit);
        return (RC_T) this;
    }
}
