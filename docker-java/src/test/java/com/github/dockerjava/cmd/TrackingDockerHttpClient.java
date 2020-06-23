package com.github.dockerjava.cmd;

import com.github.dockerjava.transport.DockerHttpClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class TrackingDockerHttpClient implements DockerHttpClient {

    static final Set<TrackedResponse> ACTIVE_RESPONSES = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private final DockerHttpClient delegate;

    TrackingDockerHttpClient(DockerHttpClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public Response execute(Request request) {
        return new TrackedResponse(delegate.execute(request)) {
            {
                synchronized (ACTIVE_RESPONSES) {
                    ACTIVE_RESPONSES.add(this);
                }
            }

            @Override
            public void close() {
                synchronized (ACTIVE_RESPONSES) {
                    ACTIVE_RESPONSES.remove(this);
                }
                super.close();
            }
        };
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    static class TrackedResponse implements Response {

        private static class AllocatedAt extends Exception {
            public AllocatedAt(String message) {
                super(message);
            }
        }

        final Exception allocatedAt = new AllocatedAt(this.toString());

        private final Response delegate;

        TrackedResponse(Response delegate) {
            this.delegate = delegate;
        }

        @Override
        public int getStatusCode() {
            return delegate.getStatusCode();
        }

        @Override
        public Map<String, List<String>> getHeaders() {
            return delegate.getHeaders();
        }

        @Override
        public InputStream getBody() {
            return delegate.getBody();
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Override
        @Nullable
        public String getHeader(@Nonnull String name) {
            return delegate.getHeader(name);
        }
    }
}
