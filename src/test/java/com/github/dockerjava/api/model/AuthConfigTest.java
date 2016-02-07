package com.github.dockerjava.api.model;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AuthConfigTest {

    private AuthConfig authConfig;

    @BeforeMethod
    public void setUp() throws Exception {
        authConfig = new AuthConfig()
                .withEmail("foo")
                .withPassword("bar")
                .withServerAddress("baz")
                .withUsername("qux");
    }

    @Test
    public void defaultServerAddress() throws Exception {
        assertEquals(new AuthConfig().getServerAddress(), "https://index.docker.io/v1/");
    }
}
