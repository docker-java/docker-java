package com.github.dockerjava.core.async;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.github.dockerjava.api.async.ResultCallback;
import com.google.common.base.Throwables;

/**
 * Implementation of {@link ResultCallback} with the single result event expected.
 */
public class AsyncResultCallback<A_RES_T> implements ResultCallback<A_RES_T> {

    private A_RES_T result = null;

    private final CountDownLatch resultReady = new CountDownLatch(1);

    private Closeable stream;

    private boolean closed = false;

    private Throwable firstError = null;

    @Override
    public void onStart(Closeable stream) {
        this.stream = stream;
        closed = false;
    }

    @Override
    public void onNext(A_RES_T object) {
        onResult(object);
    }

    private void onResult(A_RES_T object) {
        if (resultReady.getCount() == 0) {
            throw new IllegalStateException("Result has already been set");
        }

        try {
            result = object;
        } finally {
            resultReady.countDown();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (closed) return;

        if (firstError == null) {
            firstError = throwable;
        }

        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        try {
            closed = true;
            if (stream != null) {
                stream.close();
            }
        } finally {
            resultReady.countDown();
        }
    }

    /**
     * Blocks until {@link ResultCallback#onNext(Object)} was called for the first time
     */
    @SuppressWarnings("unchecked")
    public A_RES_T awaitResult() {
        try {
            resultReady.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        rethrowFirstError();
        return result;
    }

    private void rethrowFirstError() {
        if (firstError != null) {
            // this call throws a RuntimeException
            throw Throwables.propagate(firstError);
        }
    }
}
