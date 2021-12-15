package com.github.dockerjava.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.BadRequestException;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.InternalServerErrorException;
import com.github.dockerjava.api.exception.NotAcceptableException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.transport.DockerHttpClient;
import org.apache.commons.io.IOUtils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

class DefaultInvocationBuilder implements InvocationBuilder {

    private final DockerHttpClient.Request.Builder requestBuilder;
    private final DockerHttpClient dockerHttpClient;
    private final ObjectMapper objectMapper;

    DefaultInvocationBuilder(DockerHttpClient dockerHttpClient, ObjectMapper objectMapper, String path) {
        this.requestBuilder = DockerHttpClient.Request.builder().path(path);
        this.dockerHttpClient = dockerHttpClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public DefaultInvocationBuilder accept(MediaType mediaType) {
        return header("accept", mediaType.getMediaType());
    }

    @Override
    public DefaultInvocationBuilder header(String name, String value) {
        requestBuilder.putHeader(name, value);
        return this;
    }

    @Override
    public void delete() {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.DELETE)
            .build();

        execute(request).close();
    }

    @Override
    public void get(ResultCallback<Frame> resultCallback) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.GET)
            .build();

        executeAndStream(
            request,
            resultCallback,
            new FramedInputStreamConsumer(resultCallback)
        );
    }

    @Override
    public <T> T get(TypeReference<T> typeReference) {
        try (InputStream inputStream = get()) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void get(TypeReference<T> typeReference, ResultCallback<T> resultCallback) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.GET)
            .build();

        executeAndStream(
            request,
            resultCallback,
            new JsonSink<>(typeReference, resultCallback)
        );
    }

    @Override
    public InputStream post(Object entity) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.POST)
            .putHeader("content-type", "application/json")
            .bodyBytes(encode(entity))
            .build();

        DockerHttpClient.Response response = execute(request);
        return new FilterInputStream(response.getBody()) {
            @Override
            public void close() throws IOException {
                try {
                    super.close();
                } finally {
                    response.close();
                }
            }
        };
    }

    @Override
    public <T> T post(Object entity, TypeReference<T> typeReference) {
        try {
            DockerHttpClient.Request request = requestBuilder
                .method(DockerHttpClient.Request.Method.POST)
                .putHeader("content-type", "application/json")
                .bodyBytes(encode(entity))
                .build();

            try (DockerHttpClient.Response response = execute(request)) {
                return objectMapper.readValue(response.getBody(), typeReference);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void post(Object entity, TypeReference<T> typeReference, ResultCallback<T> resultCallback) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.POST)
            .putHeader("content-type", "application/json")
            .bodyBytes(encode(entity))
            .build();

        executeAndStream(
            request,
            resultCallback,
            new JsonSink<>(typeReference, resultCallback)
        );
    }

    @Override
    public <T> T post(TypeReference<T> typeReference, InputStream body) {
        try (InputStream inputStream = post(body)) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void post(Object entity, InputStream stdin, ResultCallback<Frame> resultCallback) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.POST)
            .putHeader("content-type", "application/json")
            .bodyBytes(encode(entity))
            .hijackedInput(stdin)
            .build();

        executeAndStream(
            request,
            resultCallback,
            new FramedInputStreamConsumer(resultCallback)
        );
    }

    @Override
    public <T> void post(TypeReference<T> typeReference, ResultCallback<T> resultCallback, InputStream body) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.POST)
            .body(body)
            .build();

        executeAndStream(
            request,
            resultCallback,
            new JsonSink<>(typeReference, resultCallback)
        );
    }

    @Override
    public void postStream(InputStream body) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.POST)
            .body(body)
            .build();

        execute(request).close();
    }

    @Override
    public InputStream get() {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.GET)
            .build();

        DockerHttpClient.Response response = execute(request);
        return new FilterInputStream(response.getBody()) {
            @Override
            public void close() throws IOException {
                try {
                    super.close();
                } finally {
                    response.close();
                }
            }
        };
    }

    @Override
    public void put(InputStream body, MediaType mediaType) {
        DockerHttpClient.Request request = requestBuilder
            .method(DockerHttpClient.Request.Method.PUT)
            .putHeader("content-type", mediaType.toString())
            .body(body)
            .build();

        execute(request).close();
    }

    protected DockerHttpClient.Response execute(DockerHttpClient.Request request) {
        try {
            DockerHttpClient.Response response = dockerHttpClient.execute(request);
            int statusCode = response.getStatusCode();
            if (statusCode < 200 || statusCode > 299) {
                try {
                    String body = IOUtils.toString(response.getBody(), StandardCharsets.UTF_8);
                    switch (statusCode) {
                        case 304:
                            throw new NotModifiedException(body);
                        case 400:
                            throw new BadRequestException(body);
                        case 401:
                            throw new UnauthorizedException(body);
                        case 404:
                            throw new NotFoundException(body);
                        case 406:
                            throw new NotAcceptableException(body);
                        case 409:
                            throw new ConflictException(body);
                        case 500:
                            throw new InternalServerErrorException(body);
                        default:
                            throw new DockerException(body, statusCode);
                    }
                } finally {
                    response.close();
                }
            } else {
                return response;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> void executeAndStream(
        DockerHttpClient.Request request,
        ResultCallback<T> callback,
        Consumer<DockerHttpClient.Response> sourceConsumer
    ) {
        Thread thread = new Thread(() -> {
            Thread streamingThread = Thread.currentThread();
            try (DockerHttpClient.Response response = execute(request)) {
                callback.onStart(() -> {
                    streamingThread.interrupt();
                    response.close();
                });

                sourceConsumer.accept(response);
                callback.onComplete();
            } catch (Exception e) {
                callback.onError(e);
            }
        }, "docker-java-stream-" + Objects.hashCode(request));
        thread.setDaemon(true);

        thread.start();
    }

    private byte[] encode(Object entity) {
        if (entity == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsBytes(entity);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private class JsonSink<T> implements Consumer<DockerHttpClient.Response> {

        private final TypeReference<T> typeReference;

        private final ResultCallback<T> resultCallback;

        JsonSink(TypeReference<T> typeReference, ResultCallback<T> resultCallback) {
            this.typeReference = typeReference;
            this.resultCallback = resultCallback;
        }

        @Override
        public void accept(DockerHttpClient.Response response) {
            try {
                InputStream body = response.getBody();
                MappingIterator<Object> iterator = objectMapper.readerFor(typeReference).readValues(body);
                while (iterator.hasNextValue()) {
                    resultCallback.onNext((T) iterator.nextValue());
                }
            } catch (Exception e) {
                resultCallback.onError(e);
            }
        }
    }
}
