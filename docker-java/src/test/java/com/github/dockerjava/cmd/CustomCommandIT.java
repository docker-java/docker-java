package com.github.dockerjava.cmd;

import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient.Request;
import com.github.dockerjava.transport.DockerHttpClient.Response;
import org.apache.commons.io.IOUtils;
import org.junit.Assume;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CustomCommandIT extends CmdIT {

    @Test
    public void testCustomCommand() throws Exception {
        DockerHttpClient httpClient = ((DockerClientImpl) dockerRule.getClient()).getHttpClient();

        Assume.assumeNotNull(httpClient);

        Request request = Request.builder()
            .method(Request.Method.GET)
            .path("/_ping")
            .build();

        try (Response response = httpClient.execute(request)) {
            assertThat(response.getStatusCode(), equalTo(200));
            assertThat(IOUtils.toString(response.getBody(), StandardCharsets.UTF_8), equalTo("OK"));
        }
    }
}
