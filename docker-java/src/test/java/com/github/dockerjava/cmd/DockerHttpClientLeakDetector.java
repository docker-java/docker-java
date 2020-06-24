package com.github.dockerjava.cmd;

import org.junit.rules.ExternalResource;

public class DockerHttpClientLeakDetector extends ExternalResource {

    @Override
    protected void before() {
        synchronized (TrackingDockerHttpClient.ACTIVE_RESPONSES) {
            TrackingDockerHttpClient.ACTIVE_RESPONSES.clear();
        }
    }

    @Override
    protected void after() {
        synchronized (TrackingDockerHttpClient.ACTIVE_RESPONSES) {
            if (TrackingDockerHttpClient.ACTIVE_RESPONSES.isEmpty()) {
                return;
            }

            System.out.println("Leaked responses:");
            IllegalStateException exception = new IllegalStateException("Leaked responses!");
            exception.setStackTrace(new StackTraceElement[0]);

            TrackingDockerHttpClient.ACTIVE_RESPONSES.forEach(response -> {
                exception.addSuppressed(response.allocatedAt);
            });
            throw exception;
        }
    }
}
