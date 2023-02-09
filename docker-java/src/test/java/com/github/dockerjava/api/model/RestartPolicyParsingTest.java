package com.github.dockerjava.api.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class RestartPolicyParsingTest {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();


    @Test
    public void noRestart() {
        assertEquals(RestartPolicy.noRestart(), RestartPolicy.parse("no"));
    }

    @Test
    public void alwaysRestart() {
        assertEquals(RestartPolicy.alwaysRestart(), RestartPolicy.parse("always"));
    }

    @Test
    public void unlessStoppedRestart() {
        assertEquals(RestartPolicy.unlessStoppedRestart(), RestartPolicy.parse("unless-stopped"));
    }

    @Test
    public void onFailureRestart() {
        assertEquals(RestartPolicy.onFailureRestart(0), RestartPolicy.parse("on-failure"));
    }

    @Test
    public void onFailureRestartWithCount() {
        assertEquals(RestartPolicy.onFailureRestart(2), RestartPolicy.parse("on-failure:2"));
    }

    @Test
    public void illegalSyntax() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing RestartPolicy 'nonsense'");

        RestartPolicy.parse("nonsense");
    }

    @Test
    public void illegalRetryCount() {
        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Error parsing RestartPolicy 'on-failure:X'");

        RestartPolicy.parse("on-failure:X");
    }
}
