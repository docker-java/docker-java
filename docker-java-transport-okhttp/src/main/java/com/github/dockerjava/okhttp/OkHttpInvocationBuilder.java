package com.github.dockerjava.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import com.github.dockerjava.core.InvocationBuilder;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.connection.RealConnection;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Consumer;

class OkHttpInvocationBuilder implements InvocationBuilder {

    static final ThreadLocal<Boolean> CLOSING = ThreadLocal.withInitial(() -> false);

    private final ObjectMapper objectMapper;

    private final OkHttpClient okHttpClient;

    private final Request.Builder requestBuilder;

    OkHttpInvocationBuilder(ObjectMapper objectMapper, OkHttpClient okHttpClient, HttpUrl httpUrl) {
        this.objectMapper = objectMapper;
        this.okHttpClient = okHttpClient;

        requestBuilder = new Request.Builder()
            .url(httpUrl);
    }

    @Override
    public OkHttpInvocationBuilder accept(com.github.dockerjava.core.MediaType mediaType) {
        return header("accept", mediaType.getMediaType());
    }

    @Override
    public OkHttpInvocationBuilder header(String name, String value) {
        requestBuilder.header(name, value);
        return this;
    }

    @Override
    public void delete() {
        Request request = requestBuilder
            .delete()
            .build();

        execute(request).close();
    }

    @Override
    public void get(ResultCallback<Frame> resultCallback) {
        Request request = requestBuilder
            .get()
            .build();

        executeAndStream(
            request,
            resultCallback,
            new FramedSink(resultCallback)
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
        Request request = requestBuilder
            .get()
            .build();

        executeAndStream(
            request,
            resultCallback,
            new JsonSink<>(objectMapper, typeReference, resultCallback)
        );
    }

    @Override
    public InputStream post(Object entity) {
        try {
            Request request = requestBuilder
                    .post(RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsBytes(entity)))
                    .build();

            return execute(request).body().byteStream();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T post(Object entity, TypeReference<T> typeReference) {
        try {
            Request request = requestBuilder
                    .post(RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsBytes(entity)))
                    .build();

            try (Response response = execute(request)) {
                String inputStream = response.body().string();
                return objectMapper.readValue(inputStream, typeReference);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void post(Object entity, TypeReference<T> typeReference, ResultCallback<T> resultCallback) {
        try {
            post(typeReference, resultCallback, new ByteArrayInputStream(objectMapper.writeValueAsBytes(entity)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        final Request request;
        try {
            request = requestBuilder
                .post(RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsBytes(entity)))
                .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        OkHttpClient clientToUse = this.okHttpClient;

        if (stdin != null) {
            // FIXME there must be a better way of handling it
            clientToUse = clientToUse.newBuilder()
                .addNetworkInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    if (response.isSuccessful()) {
                        Thread thread = new Thread(() -> {
                            try {
                                Field sinkField = RealConnection.class.getDeclaredField("sink");
                                sinkField.setAccessible(true);

                                try (
                                        BufferedSink sink = (BufferedSink) sinkField.get(chain.connection());
                                        Source source = Okio.source(stdin);
                                ) {
                                    while (sink.isOpen()) {
                                        int available = stdin.available();
                                        if (available > 0) {
                                            sink.write(source, available);
                                            sink.emit();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        });
                        thread.start();
                    }
                    return response;
                })
                .build();
        }

        executeAndStream(
            clientToUse,
            request,
            resultCallback,
            new FramedSink(resultCallback)
        );
    }

    @Override
    public <T> void post(TypeReference<T> typeReference, ResultCallback<T> resultCallback, InputStream body) {
        Request request = requestBuilder
            .post(toRequestBody(body, null))
            .build();

        executeAndStream(
            request,
            resultCallback,
            new JsonSink<>(objectMapper, typeReference, resultCallback)
        );
    }

    @Override
    public void postStream(InputStream body) {
        Request request = requestBuilder
            .post(toRequestBody(body, null))
            .build();

        execute(request).close();
    }

    @Override
    public InputStream get() {
        Request request = requestBuilder
            .get()
            .build();

        return execute(request).body().byteStream();
    }

    @Override
    public void put(InputStream body, com.github.dockerjava.core.MediaType mediaType) {
        Request request = requestBuilder
            .put(toRequestBody(body, mediaType.toString()))
            .build();

        execute(request).close();
    }

    protected RequestBody toRequestBody(InputStream body, String mediaType) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                if (mediaType == null) {
                    return null;
                }
                return MediaType.parse(mediaType);
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                try (Source source = Okio.source(body)) {
                    sink.writeAll(source);
                }
            }
        };
    }

    protected Response execute(Request request) {
        return execute(okHttpClient, request);
    }

    protected Response execute(OkHttpClient okHttpClient, Request request) {
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (!response.isSuccessful()) {
                String body = response.body().string();
                switch (response.code()) {
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
                        throw new DockerException(body, response.code());
                }
            } else {
                return response;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> void executeAndStream(Request request, ResultCallback<T> callback, Consumer<BufferedSource> sourceConsumer) {
        executeAndStream(okHttpClient, request, callback, sourceConsumer);
    }

    protected <T> void executeAndStream(
            OkHttpClient okHttpClient,
            Request request,
            ResultCallback<T> callback,
            Consumer<BufferedSource> sourceConsumer
    ) {
        Thread thread = new Thread(() -> {
            try (
                    Response response = execute(okHttpClient, request.newBuilder().tag("streaming").build());
                    BufferedSource source = response.body().source();
            ) {
                callback.onStart(() -> {
                    boolean previous = CLOSING.get();
                    CLOSING.set(true);
                    try {
                        response.close();
                    } finally {
                        CLOSING.set(previous);
                    }
                });

                sourceConsumer.accept(source);
                callback.onComplete();
            } catch (Exception e) {
                callback.onError(e);
            }
        }, "tc-okhttp-stream-" + Objects.hashCode(request));
        thread.setDaemon(true);

        thread.start();
    }

    private static class JsonSink<T> implements Consumer<BufferedSource> {

        private final ObjectMapper objectMapper;

        private final TypeReference<T> typeReference;

        private final ResultCallback<T> resultCallback;

        JsonSink(ObjectMapper objectMapper, TypeReference<T> typeReference, ResultCallback<T> resultCallback) {
            this.objectMapper = objectMapper;
            this.typeReference = typeReference;
            this.resultCallback = resultCallback;
        }

        @Override
        public void accept(BufferedSource source) {
            try {
                while (true) {
                    String line = source.readUtf8Line();
                    if (line == null) {
                        break;
                    }

                    resultCallback.onNext(objectMapper.readValue(line, typeReference));
                }
            } catch (Exception e) {
                resultCallback.onError(e);
            }
        }
    }

}
