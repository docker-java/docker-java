package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

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
    public void defaultServerAddress() throws Exception {
        assertEquals(new AuthConfig().getServerAddress(), "https://index.docker.io/v1/");
    }
}
