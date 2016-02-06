package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Compares serialization results of various {@link RestartPolicy}s with what Docker (as of 1.3.3) actually sends when executing
 * <code>docker run --restart xxx</code>.
 */
public class RestartPolicy_SerializingTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    // --restart no
    public void noRestart() throws Exception {
        String json = objectMapper.writeValueAsString(RestartPolicy.noRestart());
        assertEquals(json, "{\"MaximumRetryCount\":0,\"Name\":\"\"}");
    }

    @Test
    // --restart always
    public void alwaysRestart() throws Exception {
        String json = objectMapper.writeValueAsString(RestartPolicy.alwaysRestart());
        assertEquals(json, "{\"MaximumRetryCount\":0,\"Name\":\"always\"}");
    }

    @Test
    // --restart on-failure
    public void onFailureRestart() throws Exception {
        String json = objectMapper.writeValueAsString(RestartPolicy.onFailureRestart(0));
        assertEquals(json, "{\"MaximumRetryCount\":0,\"Name\":\"on-failure\"}");
    }

    @Test
    // --restart on-failure:2
    public void onFailureRestartWithCount() throws Exception {
        String json = objectMapper.writeValueAsString(RestartPolicy.onFailureRestart(2));
        assertEquals(json, "{\"MaximumRetryCount\":2,\"Name\":\"on-failure\"}");
    }

}
