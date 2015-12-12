/*
 * Created on 23.06.2015
 */
package com.github.dockerjava.jaxrs.async;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.core.async.ResponseStreamProcessor;

/**
 *
 * @author Marcus Linke
 *
 */
public class POSTCallbackNotifier<T> extends AbstractCallbackNotifier<T> {

    Entity<?> entity = null;

    public POSTCallbackNotifier(ResponseStreamProcessor<T> responseStreamProcessor, ResultCallback<T> resultCallback,
            Builder requestBuilder, Entity<?> entity) {
        super(responseStreamProcessor, resultCallback, requestBuilder);
        this.entity = entity;
    }

    protected Response response() {
        return requestBuilder.post(entity, Response.class);
    }

}
