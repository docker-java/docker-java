package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Compares serialization results of various {@link RestartPolicy}s with what Docker (as of 1.3.3) actually sends when executing
 * <code>docker run --restart xxx</code>.
 */
public class RestartPolicySerializingTest {

    @Test
    // --restart no
    public void noRestart() throws Exception {
        String json = JSONTestHelper.getMapper().writeValueAsString(RestartPolicy.noRestart());
        assertEquals(json, "{\"MaximumRetryCount\":0,\"Name\":\"\"}");
    }

    @Test
    // --restart always
    public void alwaysRestart() throws Exception {
        String json = JSONTestHelper.getMapper().writeValueAsString(RestartPolicy.alwaysRestart());
        assertEquals(json, "{\"MaximumRetryCount\":0,\"Name\":\"always\"}");
    }

    @Test
    // --restart unless-stopped
    public void unlessStoppedRestart() throws Exception {
        String json = JSONTestHelper.getMapper().writeValueAsString(RestartPolicy.unlessStoppedRestart());
        assertEquals(json, "{\"MaximumRetryCount\":0,\"Name\":\"unless-stopped\"}");
    }

    @Test
    // --restart on-failure
    public void onFailureRestart() throws Exception {
        String json = JSONTestHelper.getMapper().writeValueAsString(RestartPolicy.onFailureRestart(0));
        assertEquals(json, "{\"MaximumRetryCount\":0,\"Name\":\"on-failure\"}");
    }

    @Test
    // --restart on-failure:2
    public void onFailureRestartWithCount() throws Exception {
        String json = JSONTestHelper.getMapper().writeValueAsString(RestartPolicy.onFailureRestart(2));
        assertEquals(json, "{\"MaximumRetryCount\":2,\"Name\":\"on-failure\"}");
    }

}
