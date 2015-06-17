/*
 * Created on 23.06.2015
 */
package com.github.dockerjava.jaxrs.async;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.core.async.ResponseStreamProcessor;

/**
 * <TODO: class description>
 *
 * @author marcus
 *
 */
public class POSTCallbackNotifier<T> extends AbstractCallbackNotifier<T> {

    public POSTCallbackNotifier(ResponseStreamProcessor<T> responseStreamProcessor, ResultCallback<T> resultCallback,
            WebTarget webTarget) {
        super(responseStreamProcessor, resultCallback, webTarget);
    }

    protected Response response() {
        return webTarget.request().post(null, Response.class);
    }

}
