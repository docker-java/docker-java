package com.github.dockerjava.core;

import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.transport.DockerHttpClient;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiVersionNegotiatorTest {

    @Test
    public void daemonNewerThanClient_pinsToClientMax() {
        // Daemon advertises a fictional 1.99; client max comes from RemoteApiVersion (1.44 today).
        DockerHttpClient httpClient = httpClientReturning(200,
                "{\"ApiVersion\":\"1.99\",\"MinAPIVersion\":\"1.12\"}");

        RemoteApiVersion negotiated = ApiVersionNegotiator.negotiate(httpClient, DockerClientConfig.getDefaultObjectMapper());

        assertThat(negotiated, equalTo(ApiVersionNegotiator.latestSupported()));
    }

    @Test
    public void daemonOlderThanClient_pinsToDaemon() {
        DockerHttpClient httpClient = httpClientReturning(200,
                "{\"ApiVersion\":\"1.30\",\"MinAPIVersion\":\"1.12\"}");

        RemoteApiVersion negotiated = ApiVersionNegotiator.negotiate(httpClient, DockerClientConfig.getDefaultObjectMapper());

        assertThat(negotiated, equalTo(RemoteApiVersion.VERSION_1_30));
    }

    @Test
    public void daemonMinAboveClientMax_pinsToDaemonMin() {
        // Synthetic daemon that requires 1.99 minimum and advertises 2.10. Both are higher than
        // anything declared in RemoteApiVersion, so the client max is below the daemon min and we
        // should pin to the daemon minimum.
        DockerHttpClient httpClient = httpClientReturning(200,
                "{\"ApiVersion\":\"2.10\",\"MinAPIVersion\":\"1.99\"}");

        RemoteApiVersion negotiated = ApiVersionNegotiator.negotiate(httpClient, DockerClientConfig.getDefaultObjectMapper());

        assertThat(negotiated, equalTo(RemoteApiVersion.parseConfig("1.99")));
    }

    @Test
    public void nonSuccessStatus_throws() {
        DockerHttpClient httpClient = httpClientReturning(500, "{}");

        DockerClientException ex = assertThrows(DockerClientException.class,
                () -> ApiVersionNegotiator.negotiate(httpClient, DockerClientConfig.getDefaultObjectMapper()));
        assertThat(ex.getMessage().contains("500"), is(true));
    }

    @Test
    public void missingApiVersion_throws() {
        DockerHttpClient httpClient = httpClientReturning(200, "{\"MinAPIVersion\":\"1.12\"}");

        DockerClientException ex = assertThrows(DockerClientException.class,
                () -> ApiVersionNegotiator.negotiate(httpClient, DockerClientConfig.getDefaultObjectMapper()));
        assertThat(ex.getMessage().contains("ApiVersion"), is(true));
    }

    private static DockerHttpClient httpClientReturning(int status, String body) {
        DockerHttpClient httpClient = mock(DockerHttpClient.class);
        DockerHttpClient.Response response = mock(DockerHttpClient.Response.class);
        when(response.getStatusCode()).thenReturn(status);
        when(response.getBody()).thenReturn(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
        when(response.getHeaders()).thenReturn(Collections.emptyMap());
        when(httpClient.execute(any(DockerHttpClient.Request.class))).thenReturn(response);
        return httpClient;
    }
}
