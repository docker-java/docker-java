/*
 * Created on 23.06.2015
 */
package com.github.dockerjava.jaxrs.async;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.core.async.ResponseStreamProcessor;

/**
 *
 * @author Marcus Linke
 *
 */
public class GETCallbackNotifier<T> extends AbstractCallbackNotifier<T> {

    public GETCallbackNotifier(ResponseStreamProcessor<T> responseStreamProcessor, ResultCallback<T> resultCallback,
            Builder requestBuilder) {
        super(responseStreamProcessor, resultCallback, requestBuilder);
    }

    protected Response response() {
        return requestBuilder.get(Response.class);
    }

}
