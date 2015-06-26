package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class RestartPolicy_ParsingTest {

    @Test
    public void noRestart() throws Exception {
        assertEquals(RestartPolicy.parse("no"), RestartPolicy.noRestart());
    }

    @Test
    public void alwaysRestart() throws Exception {
        assertEquals(RestartPolicy.parse("always"), RestartPolicy.alwaysRestart());
    }

    @Test
    public void onFailureRestart() throws Exception {
        assertEquals(RestartPolicy.parse("on-failure"), RestartPolicy.onFailureRestart(0));
    }

    @Test
    public void onFailureRestartWithCount() throws Exception {
        assertEquals(RestartPolicy.parse("on-failure:2"), RestartPolicy.onFailureRestart(2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing RestartPolicy 'nonsense'")
    public void illegalSyntax() throws Exception {
        RestartPolicy.parse("nonsense");
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Error parsing RestartPolicy 'on-failure:X'")
    public void illegalRetryCount() throws Exception {
        RestartPolicy.parse("on-failure:X");
    }
}
