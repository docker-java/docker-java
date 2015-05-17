package com.github.dockerjava.api.stream;

import java.io.Closeable;

/**
 * Object stream callback
 */
public interface ObjectStreamCallback<T> {
	/** Called when the stream starts. The passed {@link Closeable} can be used to close/interrupt the stream */
	void streamStarted(Closeable stream);
	/** Called when a stream event occurs */
    void onStream(T object);
    /** Called when an exception occurs while processing the stream */
    void onError(Throwable throwable);
    /** Called when streaming was finished either by reaching the end of the stream or by aborting it */
    void streamFinished();
    
}
