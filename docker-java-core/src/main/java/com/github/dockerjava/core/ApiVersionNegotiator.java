package com.github.dockerjava.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Performs one-shot Docker Remote API version negotiation against a running daemon.
 * <p>
 * Mirrors the behaviour of {@code moby/client.NegotiateAPIVersion}: queries {@code GET /version},
 * reads the daemon's {@code ApiVersion} and {@code MinAPIVersion}, and returns
 * {@code min(daemon ApiVersion, latest version known to docker-java)}. If that result is below the
 * daemon's {@code MinAPIVersion}, the daemon's minimum is used instead and a warning is logged —
 * matching {@code moby}'s behaviour.
 * <p>
 * Callers normally don't invoke this directly: enable
 * {@link DefaultDockerClientConfig.Builder#withApiVersionAutoNegotiation(boolean)} and the
 * {@link DockerClientImpl#getInstance(DockerClientConfig, DockerHttpClient)} factory will run
 * negotiation automatically. The class is exposed as a public utility for callers that want to
 * negotiate without going through {@code DockerClientImpl} (for example, when constructing
 * commands manually for tests).
 */
public final class ApiVersionNegotiator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiVersionNegotiator.class);

    private ApiVersionNegotiator() {
    }

    /**
     * Calls {@code GET /version} via {@code httpClient}, parses the response with {@code objectMapper},
     * and returns the negotiated {@link RemoteApiVersion}.
     *
     * @throws DockerClientException if the daemon is unreachable, returns a non-2xx status, or returns
     *                               a {@code /version} payload without a parseable {@code ApiVersion}.
     */
    @Nonnull
    public static RemoteApiVersion negotiate(@Nonnull DockerHttpClient httpClient, @Nonnull ObjectMapper objectMapper) {
        Objects.requireNonNull(httpClient, "httpClient was not specified");
        Objects.requireNonNull(objectMapper, "objectMapper was not specified");

        Version daemonVersion = fetchDaemonVersion(httpClient, objectMapper);

        RemoteApiVersion daemonApi = parseOrThrow(daemonVersion.getApiVersion(), "ApiVersion");
        RemoteApiVersion clientMax = latestSupported();

        RemoteApiVersion negotiated = daemonApi.isGreater(clientMax) ? clientMax : daemonApi;

        String minApiVersionString = daemonVersion.getMinAPIVersion();
        if (minApiVersionString != null && !minApiVersionString.isEmpty()) {
            RemoteApiVersion daemonMin = RemoteApiVersion.parseConfigWithDefault(minApiVersionString);
            if (daemonMin != RemoteApiVersion.UNKNOWN_VERSION && daemonMin.isGreater(negotiated)) {
                LOGGER.warn(
                        "Negotiated API version {} is below the daemon's minimum supported version {}. "
                                + "Pinning to the daemon's minimum; some docker-java features may not work as expected.",
                        negotiated.getVersion(), daemonMin.getVersion());
                negotiated = daemonMin;
            }
        }

        LOGGER.debug("API version auto-negotiation result: daemon={} min={} client-max={} -> {}",
                daemonApi.getVersion(),
                minApiVersionString,
                clientMax.getVersion(),
                negotiated.getVersion());

        return negotiated;
    }

    private static Version fetchDaemonVersion(DockerHttpClient httpClient, ObjectMapper objectMapper) {
        DockerHttpClient.Request request = DockerHttpClient.Request.builder()
                .method(DockerHttpClient.Request.Method.GET)
                .path("/version")
                .build();

        try (DockerHttpClient.Response response = httpClient.execute(request);
             InputStream body = response.getBody()) {
            int status = response.getStatusCode();
            if (status < 200 || status >= 300) {
                throw new DockerClientException(
                        "Failed to negotiate Docker API version: GET /version returned HTTP " + status);
            }
            return objectMapper.readValue(body, Version.class);
        } catch (IOException e) {
            throw new DockerClientException("Failed to negotiate Docker API version", e);
        }
    }

    private static RemoteApiVersion parseOrThrow(String version, String field) {
        if (version == null || version.isEmpty()) {
            throw new DockerClientException("Daemon /version response did not include a " + field);
        }
        RemoteApiVersion parsed = RemoteApiVersion.parseConfigWithDefault(version);
        if (parsed == RemoteApiVersion.UNKNOWN_VERSION) {
            throw new DockerClientException("Daemon /version returned unparseable " + field + ": " + version);
        }
        return parsed;
    }

    /**
     * The highest {@code VERSION_1_X} constant declared on {@link RemoteApiVersion}. Discovered by
     * reflection so adding new constants does not require touching this class.
     */
    static RemoteApiVersion latestSupported() {
        RemoteApiVersion highest = null;
        for (Field field : RemoteApiVersion.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isPublic(field.getModifiers())) {
                continue;
            }
            if (!RemoteApiVersion.class.equals(field.getType())) {
                continue;
            }
            try {
                RemoteApiVersion candidate = (RemoteApiVersion) field.get(null);
                if (candidate == null || candidate == RemoteApiVersion.UNKNOWN_VERSION) {
                    continue;
                }
                if (highest == null || candidate.isGreater(highest)) {
                    highest = candidate;
                }
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Unable to read " + field.getName(), e);
            }
        }
        if (highest == null) {
            throw new IllegalStateException("No VERSION_1_X constants found on RemoteApiVersion");
        }
        return highest;
    }
}
