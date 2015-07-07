package com.github.dockerjava.api.async;

import java.io.Closeable;

/**
 * Result callback
 */
public interface ResultCallback<RES_T> extends Closeable {
    /**
     * Called when the async processing starts. The passed {@link Closeable} can be used to close/interrupt the
     * processing
     */
    void onStart(Closeable closeable);

    /** Called when an async result event occurs */
    void onNext(RES_T object);

    /** Called when an exception occurs while processing */
    void onError(Throwable throwable);

    /** Called when processing was finished either by reaching the end or by aborting it */
    void onComplete();

}
