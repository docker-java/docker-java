/*
 * Created on 18.06.2015
 */
package com.github.dockerjava.core.async;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.async.ResultCallback;

/**
 *
 * @author marcus
 *
 */
public class JsonStreamProcessor<T> implements ResponseStreamProcessor<T> {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Class<T> clazz;

    public JsonStreamProcessor(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void processResponseStream(InputStream response, ResultCallback<T> resultCallback) {

        resultCallback.onStart(response);

        try {
            JsonParser jp = JSON_FACTORY.createParser(response);
            while (!jp.isClosed() && jp.nextToken() != JsonToken.END_OBJECT) {
                try {
                    resultCallback.onNext(OBJECT_MAPPER.readValue(jp, clazz));
                } catch (Exception e) {
                    resultCallback.onError(e);
                }
            }
        } catch (Throwable t) {
            resultCallback.onError(t);
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                resultCallback.onError(e);
            } finally {
                resultCallback.onComplete();
            }
        }

    }
}
