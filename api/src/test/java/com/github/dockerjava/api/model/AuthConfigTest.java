package com.github.dockerjava.api.model;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AuthConfigTest {

    private AuthConfig authConfig;

    @BeforeMethod
    public void setUp() throws Exception {
        authConfig = new AuthConfig();
        authConfig.setEmail("foo");
        authConfig.setPassword("bar");
        authConfig.setServerAddress("baz");
        authConfig.setUsername("qux");
    }

    @Test
    public void string() throws Exception {
        Assert.assertEquals(authConfig.toString(),
                "AuthConfig{username='qux', password='bar', email='foo', serverAddress='baz'}");
    }

    @Test
    public void defaultServerAddress() throws Exception {
        Assert.assertEquals(new AuthConfig().getServerAddress(), "https://index.docker.io/v1/");
    }
}