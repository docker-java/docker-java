package com.github.dockerjava.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.async.ResultCallbackTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

public interface InvocationBuilder {

    InvocationBuilder accept(MediaType mediaType);

    InvocationBuilder header(String name, String value);

    void delete();

    void get(ResultCallback<Frame> resultCallback);

    <T> T get(TypeReference<T> typeReference);

    <T> void get(TypeReference<T> typeReference, ResultCallback<T> resultCallback);

    InputStream post(Object entity);

    void post(Object entity, InputStream stdin, ResultCallback<Frame> resultCallback);

    <T> T post(Object entity, TypeReference<T> typeReference);

    <T> void post(Object entity, TypeReference<T> typeReference, ResultCallback<T> resultCallback);

    <T> T post(TypeReference<T> typeReference, InputStream body);

    <T> void post(TypeReference<T> typeReference, ResultCallback<T> resultCallback, InputStream body);

    void postStream(InputStream body);

    InputStream get();

    void put(InputStream body, MediaType mediaType);

    /**
     * Implementation of {@link ResultCallback} with the single result event expected.
     */
    class AsyncResultCallback<A_RES_T>
            extends ResultCallbackTemplate<InvocationBuilder.AsyncResultCallback<A_RES_T>, A_RES_T> {

        private A_RES_T result = null;

        private final CountDownLatch resultReady = new CountDownLatch(1);

        @Override
        public void onNext(A_RES_T object) {
            onResult(object);
        }

        private void onResult(A_RES_T object) {
            if (resultReady.getCount() == 0) {
                throw new IllegalStateException("Result has already been set");
            }

            try {
                result = object;
            } finally {
                resultReady.countDown();
            }
        }

        @Override
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                resultReady.countDown();
            }
        }

        /**
         * Blocks until {@link ResultCallback#onNext(Object)} was called for the first time
         */
        @SuppressWarnings("unchecked")
        public A_RES_T awaitResult() {
            try {
                resultReady.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            throwFirstError();
            return result;
        }
    }
}
