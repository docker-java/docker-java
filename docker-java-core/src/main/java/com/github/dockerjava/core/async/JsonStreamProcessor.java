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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.core.DefaultDockerClientConfig;

/**
 *
 * @author Marcus Linke
 *
 */
public class JsonStreamProcessor<T> implements ResponseStreamProcessor<T> {

    private static final JsonFactory JSON_FACTORY = new JsonFactory();

    private final Class<T> clazz;

    private final ObjectMapper objectMapper;

    @Deprecated
    public JsonStreamProcessor(Class<T> clazz) {
        this(
                DefaultDockerClientConfig.createDefaultConfigBuilder().build().getObjectMapper(),
                clazz
        );
    }

    public JsonStreamProcessor(ObjectMapper objectMapper, Class<T> clazz) {
        this.clazz = clazz;
        this.objectMapper = objectMapper.copy().enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    }

    @Override
    public void processResponseStream(InputStream response, ResultCallback<T> resultCallback) {

        resultCallback.onStart(response);

        try {
            JsonParser jp = JSON_FACTORY.createParser(response);
            Boolean closed = jp.isClosed();
            JsonToken nextToken = jp.nextToken();
            while (!closed && nextToken != null && nextToken != JsonToken.END_OBJECT) {
                try {
                    ObjectNode objectNode = objectMapper.readTree(jp);
                    // exclude empty item serialization into class #461
                    if (!objectNode.isEmpty(null)) {
                        T next = objectMapper.treeToValue(objectNode, clazz);
                        resultCallback.onNext(next);
                    }
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
