/*
 * Created on 18.06.2015
 */
package com.github.dockerjava.core.async;

import java.io.InputStream;

import com.github.dockerjava.api.async.ResultCallback;

/**
 *
 * @author Marcus Linke
 *
 */
public interface ResponseStreamProcessor<T> {

    void processResponseStream(InputStream response, ResultCallback<T> resultCallback);

}
