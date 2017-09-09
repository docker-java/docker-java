package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;

@RunWith(Parameterized.class)
public class RestartPolicyToStringTest {

    @Parameterized.Parameters(name = "{0}")
    public static Object[][] restartPolicies() {
        return new Object[][] { {"no"}, {"always"}, {"unless-stopped"}, {"on-failure"}, {"on-failure:2"}};
    }

    @Parameterized.Parameter
    public String policy;

    @Test
    public void serializationWithoutCount() throws Exception {
        assertEquals(RestartPolicy.parse(policy).toString(), policy);
    }

}
