package com.github.dockerjava.netty;

import static org.testng.Assert.assertEquals;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Alexander Koshevoy
 */
public class WebTargetTest {
    @Mock private ChannelProvider channelProvider;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void verifyImmutability() throws Exception {
        WebTarget emptyWebTarget = new WebTarget(channelProvider);

        WebTarget initWebTarget = emptyWebTarget.path("/containers/{id}/attach").resolveTemplate("id", "d03da378b592")
                .queryParam("logs", "true");

        WebTarget anotherWebTarget = emptyWebTarget.path("/containers/{id}/attach")
                .resolveTemplate("id", "2cfada4e3c07").queryParam("stdin", "true");

        assertEquals(new WebTarget(channelProvider), emptyWebTarget);

        assertEquals(new WebTarget(channelProvider).path("/containers/d03da378b592/attach")
                .queryParam("logs", "true"), initWebTarget);

        assertEquals(new WebTarget(channelProvider).path("/containers/2cfada4e3c07/attach")
                .queryParam("stdin", "true"), anotherWebTarget);
    }
}
