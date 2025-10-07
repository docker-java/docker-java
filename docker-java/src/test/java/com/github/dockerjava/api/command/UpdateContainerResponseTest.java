package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.UpdateContainerResponse;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static com.github.dockerjava.test.serdes.JSONTestHelper.testRoundTrip;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UpdateContainerResponseTest {
    @Test
    public void roundTrip_empty() throws IOException {
        UpdateContainerResponse response = testRoundTrip(CommandJSONSamples.updateContainerResponse_empty, UpdateContainerResponse.class);
        assertNull(response.getWarnings());
    }

    @Test
    public void roundTrip_warnings() throws IOException {
        UpdateContainerResponse response = testRoundTrip(CommandJSONSamples.updateContainerResponse_warnings,
            UpdateContainerResponse.class);

        List<String> warnings = response.getWarnings();
        assertNotNull(warnings);
        assertEquals(1, warnings.size());

        final String warning = warnings.get(0);
        assertEquals("Published ports are discarded when using host network mode", warning);
    }
}
