package com.github.dockerjava.api.async;

import java.io.Closeable;

/**
 * Stream callback
 */
public interface StreamCallback<T> {
	/** Called when the stream starts. The passed {@link Closeable} can be used to close/interrupt the stream */
	void onStart(Closeable stream);
	/** Called when a stream event occurs */
    void onStream(T object);
    /** Called when an exception occurs while processing the stream */
    void onError(Throwable throwable);
    /** Called when streaming was finished either by reaching the end of the stream or by aborting it */
    void onFinish();

}
