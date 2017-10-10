package com.github.dockerjava.api.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class RestartPolicyParsingTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void noRestart() throws Exception {
        assertEquals(RestartPolicy.parse("no"), RestartPolicy.noRestart());
    }

    @Test
    public void alwaysRestart() throws Exception {
        assertEquals(RestartPolicy.parse("always"), RestartPolicy.alwaysRestart());
    }

    @Test
    public void unlessStoppedRestart() throws Exception {
        assertEquals(RestartPolicy.parse("unless-stopped"), RestartPolicy.unlessStoppedRestart());
    }

    @Test
    public void onFailureRestart() throws Exception {
        assertEquals(RestartPolicy.parse("on-failure"), RestartPolicy.onFailureRestart(0));
    }

    @Test
    public void onFailureRestartWithCount() throws Exception {
        assertEquals(RestartPolicy.parse("on-failure:2"), RestartPolicy.onFailureRestart(2));
    }

    @Test
    public void illegalSyntax() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing RestartPolicy 'nonsense'");

        RestartPolicy.parse("nonsense");
    }

    @Test
    public void illegalRetryCount() throws Exception {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing RestartPolicy 'on-failure:X'");

        RestartPolicy.parse("on-failure:X");
    }
}
