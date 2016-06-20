/*
 * Created on 17.06.2015
 */
package com.github.dockerjava.jaxrs.async;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.core.async.ResponseStreamProcessor;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public abstract class AbstractCallbackNotifier<T> implements Callable<Void> {

    private final ResponseStreamProcessor<T> responseStreamProcessor;

    private final ResultCallback<T> resultCallback;

    private static final ThreadFactory FACTORY =
            new ThreadFactoryBuilder().setDaemon(true).setNameFormat("dockerjava-jaxrs-async-%d").build();

    protected final Builder requestBuilder;

    protected AbstractCallbackNotifier(ResponseStreamProcessor<T> responseStreamProcessor,
            ResultCallback<T> resultCallback, Builder requestBuilder) {
        checkNotNull(requestBuilder, "An WebTarget must be provided");
        checkNotNull(responseStreamProcessor, "A ResponseStreamProcessor must be provided");
        this.responseStreamProcessor = responseStreamProcessor;
        this.resultCallback = resultCallback;
        this.requestBuilder = requestBuilder;
    }

    @Override
    public Void call() throws Exception {

        Response response = null;

        try {
            response = response();
        } catch (ProcessingException e) {
            if (resultCallback != null) {
                resultCallback.onError(e.getCause());
            }
            return null;
        } catch (Exception e) {
            if (resultCallback != null) {
                resultCallback.onError(e);
            }
            return null;
        }

        try (InputStream inputStream = new WrappedResponseInputStream(response)) {

            if (resultCallback != null) {
                responseStreamProcessor.processResponseStream(inputStream, resultCallback);
            }

            return null;
        } catch (Exception e) {
            if (resultCallback != null) {
                resultCallback.onError(e);
            }

            return null;
        }
    }

    protected abstract Response response();

    public static <T> Future<Void> startAsyncProcessing(AbstractCallbackNotifier<T> callbackNotifier) {

        ExecutorService executorService = Executors.newSingleThreadExecutor(FACTORY);
        Future<Void> response = executorService.submit(callbackNotifier);
        executorService.shutdown();
        return response;
    }
}
