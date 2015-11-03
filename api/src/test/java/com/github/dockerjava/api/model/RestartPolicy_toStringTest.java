package com.github.dockerjava.api.model;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RestartPolicy_toStringTest {

    @DataProvider(name = "input")
    public Object[][] restartPolicies() {
        return new Object[][] { { "no" }, { "always" }, { "on-failure" }, { "on-failure:2" } };
    }

    @Test(dataProvider = "input")
    public void serializationWithoutCount(String policy) throws Exception {
        assertEquals(RestartPolicy.parse(policy).toString(), policy);
    }

}
