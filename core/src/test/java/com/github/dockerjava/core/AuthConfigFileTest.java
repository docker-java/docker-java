/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.github.dockerjava.api.model.AuthConfig;

public class AuthConfigFileTest {

    private final File FILESROOT = new File(Thread.currentThread().getContextClassLoader()
            .getResource("testAuthConfigFile").getFile());

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "The Auth Config file is empty")
    public void emptyFile() throws IOException {
        runTest("emptyFile");
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "The Auth Config file is empty")
    public void tooSmallFile() throws IOException {
        runTest("tooSmallFile");
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Invalid auth configuration file")
    public void invalidJsonInvalidAuth() throws IOException {
        runTest("invalidJsonInvalidAuth");
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Invalid Auth config file")
    public void invalidLegacyAuthLine() throws IOException {
        runTest("invalidLegacyAuthLine");
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Invalid auth configuration file")
    public void invalidLegacyInvalidAuth() throws IOException {
        runTest("invalidLegacyInvalidAuth");
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Invalid Auth config file")
    public void invalidLegacyEmailLine() throws IOException {
        runTest("invalidLegacyEmailLine");
    }

    @Test
    public void validJson() throws IOException {
        AuthConfig authConfig1 = new AuthConfig();
        authConfig1.setEmail("foo@example.com");
        authConfig1.setUsername("foo");
        authConfig1.setPassword("bar");
        authConfig1.setServerAddress("quay.io");

        AuthConfig authConfig2 = new AuthConfig();
        authConfig2.setEmail("moo@example.com");
        authConfig2.setUsername("foo1");
        authConfig2.setPassword("bar1");
        authConfig2.setServerAddress(AuthConfig.DEFAULT_SERVER_ADDRESS);

        AuthConfigFile expected = new AuthConfigFile();
        expected.addConfig(authConfig1);
        expected.addConfig(authConfig2);

        Assert.assertEquals(runTest("validJson"), expected);

    }

    @Test
    public void validLegacy() throws IOException {
        AuthConfig authConfig = new AuthConfig();
        authConfig.setEmail("foo@example.com");
        authConfig.setUsername("foo");
        authConfig.setPassword("bar");
        authConfig.setServerAddress(AuthConfig.DEFAULT_SERVER_ADDRESS);

        AuthConfigFile expected = new AuthConfigFile();
        expected.addConfig(authConfig);

        Assert.assertEquals(runTest("validLegacy"), expected);
    }

    @Test
    public void nonExistent() throws IOException {
        AuthConfigFile expected = new AuthConfigFile();
        Assert.assertEquals(runTest("idontexist"), expected);
    }

    private AuthConfigFile runTest(String testFileName) throws IOException {
        return AuthConfigFile.loadConfig(new File(FILESROOT, testFileName));
    }

}
