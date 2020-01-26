package com.github.dockerjava.jaxrs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.async.FrameStreamProcessor;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.GETCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;
import com.github.dockerjava.jaxrs.util.WrappedResponseInputStream;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

class JerseyInvocationBuilder implements InvocationBuilder {

    private final ObjectMapper objectMapper;

    private final Invocation.Builder resource;

    JerseyInvocationBuilder(ObjectMapper objectMapper, Invocation.Builder resource) {
        this.objectMapper = objectMapper;
        this.resource = resource;
    }

    @Override
    public InvocationBuilder accept(MediaType mediaType) {
        resource.accept(mediaType.getMediaType());
        return this;
    }

    @Override
    public InvocationBuilder header(String name, String value) {
        resource.header(name, value);
        return this;
    }

    @Override
    public void delete() {
        resource.delete().close();
    }

    @Override
    public void get(ResultCallback<Frame> resultCallback) {
        try {
            GETCallbackNotifier<Frame> getCallbackNotifier = new GETCallbackNotifier<>(
                    new FrameStreamProcessor(),
                    resultCallback,
                    resource
            );
            getCallbackNotifier.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T get(TypeReference<T> typeReference) {
        try (Response response = resource.get()) {
            return objectMapper.readValue(response.readEntity(InputStream.class), typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void get(TypeReference<T> typeReference, ResultCallback<T> resultCallback) {
        try {
            GETCallbackNotifier<T> getCallbackNotifier = new GETCallbackNotifier<T>(
                    new JsonStreamProcessor<>(objectMapper, typeReference),
                    resultCallback,
                    resource
            );
            getCallbackNotifier.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream post(Object entity) {
        return new WrappedResponseInputStream(resource.post(
                toEntity(entity, javax.ws.rs.core.MediaType.APPLICATION_JSON)
        ));
    }

    @Override
    public void post(Object entity, InputStream stdin, ResultCallback<Frame> resultCallback) {
        if (stdin != null) {
            throw new UnsupportedOperationException("Passing stdin to the container is currently not supported.");
        }

        POSTCallbackNotifier<Frame> postCallbackNotifier = new POSTCallbackNotifier<>(
                new FrameStreamProcessor(),
                resultCallback,
                resource,
                toEntity(entity, javax.ws.rs.core.MediaType.APPLICATION_JSON)
        );

        postCallbackNotifier.start();
    }

    @Override
    public <T> T post(Object entity, TypeReference<T> typeReference) {
        Response response = resource.post(
                toEntity(entity, javax.ws.rs.core.MediaType.APPLICATION_JSON)
        );

        if (response.getStatus() == 401) {
            throw new UnauthorizedException("Unauthorized");
        }

        try (InputStream inputStream = response.readEntity(InputStream.class)) {
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void post(Object entity, TypeReference<T> typeReference, ResultCallback<T> resultCallback) {
        try {
            POSTCallbackNotifier<T> postCallbackNotifier = new POSTCallbackNotifier<>(
                    new JsonStreamProcessor<>(objectMapper, typeReference),
                    resultCallback,
                    resource,
                    toEntity(entity, javax.ws.rs.core.MediaType.APPLICATION_JSON)
            );
            postCallbackNotifier.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T post(TypeReference<T> typeReference, InputStream body) {
        try (
                Response response = resource.post(
                        toEntity(body, javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)
                )
        ) {
            InputStream inputStream = response.readEntity(InputStream.class);
            return objectMapper.readValue(inputStream, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> void post(TypeReference<T> typeReference, ResultCallback<T> resultCallback, InputStream body) {
        try {
            POSTCallbackNotifier<T> postCallbackNotifier = new POSTCallbackNotifier<T>(
                    new JsonStreamProcessor<>(objectMapper, typeReference),
                    resultCallback,
                    resource,
                    toEntity(body, "application/tar")
            );
            postCallbackNotifier.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postStream(InputStream body) {
        resource.post(toEntity(body, javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM)).close();
    }

    @Override
    public InputStream get() {
        return new WrappedResponseInputStream(resource.get());
    }

    @Override
    public void put(InputStream body, MediaType mediaType) {
        resource.put(toEntity(body, mediaType.getMediaType())).close();
    }

    private static <T> Entity<T> toEntity(T entity, String mediaType) {
        if (entity == null) {
            return null;
        }

        return Entity.entity(entity, mediaType);
    }
}
