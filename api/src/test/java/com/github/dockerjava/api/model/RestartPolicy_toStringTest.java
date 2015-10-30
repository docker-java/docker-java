package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RestartPolicy_toStringTest {

    @DataProvider(name = "input")
    public Object[][] restartPolicies() {
        return new Object[][] { { "no" }, { "always" }, { "on-failure" }, { "on-failure:2" } };
    }

    @Test(dataProvider = "input")
    public void serializationWithoutCount(String policy) throws Exception {
        Assert.assertEquals(RestartPolicy.parse(policy).toString(), policy);
    }

}
