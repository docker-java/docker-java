/*
 * Created on 23.06.2015
 */
package com.github.dockerjava.jaxrs.jersey.jaxrs.async;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.core.async.ResponseStreamProcessor;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

/**
 *
 * @author marcus
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
