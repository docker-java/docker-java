package com.github.dockerjava.core;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface DockerHttpClient extends Closeable {

    Response execute(Request request);

    interface Response extends Closeable {

        int getStatusCode();

        Map<String, List<String>> getHeaders();

        InputStream getBody();

        @Override
        void close();
    }

    @Value.Immutable
    @Value.Style(
        visibility = Value.Style.ImplementationVisibility.PACKAGE,
        overshadowImplementation = true,
        depluralize = true
    )
    abstract class Request {

        public enum Method {
            GET,
            POST,
            PUT,
            DELETE,
            OPTIONS,
            PATCH,
        }

        public static class Builder extends ImmutableRequest.Builder {

            public Builder method(Method method) {
                return method(method.name());
            }
        }

        public static Builder builder() {
            return new Builder();
        }

        public abstract String method();

        public abstract String path();

        @Nullable
        public abstract InputStream body();

        @Nullable
        public abstract InputStream hijackedInput();

        public abstract Map<String, String> headers();
    }
}
