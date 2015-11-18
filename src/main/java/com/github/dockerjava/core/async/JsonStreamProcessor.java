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
        OBJECT_MAPPER.configure(com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        try {
            JsonParser jp = JSON_FACTORY.createParser(response);
            Boolean closed = jp.isClosed();
            JsonToken nextToken = jp.nextToken();
            while (!closed && nextToken != null && nextToken != JsonToken.END_OBJECT) {
                try {
                    T next = OBJECT_MAPPER.readValue(jp, clazz);
                    resultCallback.onNext(next);
                } catch (Exception e) {
                    resultCallback.onError(e);
                }

                closed = jp.isClosed();
                nextToken = jp.nextToken();
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
