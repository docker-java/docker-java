/*
 * Created on 18.06.2015
 */
package com.github.dockerjava.core.async;

import com.github.dockerjava.api.async.ResultCallback;

import java.io.InputStream;

/**
 *
 * @author marcus
 *
 */
public interface ResponseStreamProcessor<T> {

    void processResponseStream(InputStream response, ResultCallback<T> resultCallback);

}
