/*
 * Created on 16.06.2015
 */
package com.github.dockerjava.api.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Abstract template implementation of {@link ResultCallback}
 *
 * @author Marcus Linke
 *
 */
public abstract class ResultCallbackTemplate<RC_T extends ResultCallback<A_RES_T>, A_RES_T> implements
        ResultCallback<A_RES_T> {

    private static final Consumer<Throwable> ERROR_LOGGER;

    static {
        Consumer<Throwable> errorLogger = e -> {
        };
        try {
            if (Class.forName("org.slf4j.LoggerFactory") != null) {
                Logger logger = LoggerFactory.getLogger(ResultCallbackTemplate.class);
                errorLogger = e -> logger.error("Error during callback", e);
            }
        } catch (ClassNotFoundException ignored) {
        }
        ERROR_LOGGER = errorLogger;
    }

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
            ERROR_LOGGER.accept(throwable);
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
           try {
               if (stream != null) {
                   stream.close();
               }
           } finally {
               completed.countDown();
           }
        }
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitCompletion() throws InterruptedException {
        completed.await();
        // eventually (re)throws RuntimeException
        throwFirstError();
        return (RC_T) this;
    }

    /**
     * Blocks until {@link ResultCallback#onComplete()} was called or the given timeout occurs
     * @return {@code true} if completed and {@code false} if the waiting time elapsed
     *         before {@link ResultCallback#onComplete()} was called.
     */
    public boolean awaitCompletion(long timeout, TimeUnit timeUnit) throws InterruptedException {
        boolean result = completed.await(timeout, timeUnit);
        throwFirstError();
        return result;
    }

    /**
     * Blocks until {@link ResultCallback#onStart(Closeable)} was called.
     * {@link ResultCallback#onStart(Closeable)} is called when the request was processed on the server
     * side and the response is incoming.
     */
    @SuppressWarnings("unchecked")
    public RC_T awaitStarted() throws InterruptedException {
        started.await();
        return (RC_T) this;
    }

    /**
     * Blocks until {@link ResultCallback#onStart(Closeable)} was called or the given timeout occurs.
     * {@link ResultCallback#onStart(Closeable)} is called when the request was processed on the server side
     * and the response is incoming.
     * @return {@code true} if started and {@code false} if the waiting time elapsed
     *         before {@link ResultCallback#onStart(Closeable)} was called.
     */
    public boolean awaitStarted(long timeout, TimeUnit timeUnit) throws InterruptedException {
        return started.await(timeout, timeUnit);
    }

    /**
     * Throws the first occurred error as a runtime exception
     * @throws com.github.dockerjava.api.exception.DockerException The first docker based Error
     * @throws RuntimeException on any other occurred error
     */
    protected void throwFirstError() {
        if (firstError != null) {
            if (firstError instanceof Error) {
              throw (Error) firstError;
            }
            if (firstError instanceof RuntimeException) {
              throw (RuntimeException) firstError;
            }
            throw new RuntimeException(firstError);
        }
    }
}
