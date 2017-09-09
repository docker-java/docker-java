/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import java.io.File;
import java.io.IOException;


import com.github.dockerjava.api.model.AuthConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class AuthConfigFileTest {
    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private final File FILESROOT = new File(Thread.currentThread().getContextClassLoader()
            .getResource("testAuthConfigFile").getFile());

    @Test
    public void emptyFile() throws IOException {
        expectedEx.expect(IOException.class);
        expectedEx.expectMessage("The Auth Config file is empty");

        runTest("emptyFile");
    }

    @Test
    public void tooSmallFile() throws IOException {
        expectedEx.expect(IOException.class);
        expectedEx.expectMessage("The Auth Config file is empty");

        runTest("tooSmallFile");
    }

    @Test
    public void invalidJsonInvalidAuth() throws IOException {
        expectedEx.expect(IOException.class);
        expectedEx.expectMessage("Invalid auth configuration file");

        runTest("invalidJsonInvalidAuth");
    }

    @Test
    public void invalidLegacyAuthLine() throws IOException {
        expectedEx.expect(IOException.class);
        expectedEx.expectMessage("Invalid Auth config file");

        runTest("invalidLegacyAuthLine");
    }

    @Test
    public void invalidLegacyInvalidAuth() throws IOException {
        expectedEx.expect(IOException.class);
        expectedEx.expectMessage("Invalid auth configuration file");

        runTest("invalidLegacyInvalidAuth");
    }

    @Test
    public void invalidLegacyEmailLine() throws IOException {
        expectedEx.expect(IOException.class);
        expectedEx.expectMessage("Invalid Auth config file");

        runTest("invalidLegacyEmailLine");
    }

    @Test
    public void validJson() throws IOException {
        AuthConfig authConfig1 = new AuthConfig()
                .withEmail("foo@example.com")
                .withUsername("foo")
                .withPassword("bar")
                .withRegistryAddress("quay.io");

        AuthConfig authConfig2 = new AuthConfig()
                .withEmail("moo@example.com")
                .withUsername("foo1")
                .withPassword("bar1")
                .withRegistryAddress(AuthConfig.DEFAULT_SERVER_ADDRESS);

        AuthConfigFile expected = new AuthConfigFile();
        expected.addConfig(authConfig1);
        expected.addConfig(authConfig2);

        assertEquals(runTest("validJson.json"), expected);

    }

    @Test
    public void validJsonWithUnknown() throws IOException {
        AuthConfig authConfig1 = new AuthConfig()
                .withEmail("foo@example.com")
                .withUsername("foo")
                .withPassword("bar")
                .withRegistryAddress("quay.io");

        AuthConfigFile expected = new AuthConfigFile();
        expected.addConfig(authConfig1);
        runTest("validJsonWithUnknown.json");
    }

    @Test
    public void validJsonWithOnlyUnknown() throws IOException {
        AuthConfigFile expected = new AuthConfigFile();
        AuthConfigFile actual = runTest("validJsonWithOnlyUnknown.json");
        assertEquals(actual, expected);
    }

    @Test
    public void validLegacy() throws IOException {
        AuthConfig authConfig = new AuthConfig()
                .withEmail("foo@example.com")
                .withUsername("foo")
                .withPassword("bar")
                .withRegistryAddress(AuthConfig.DEFAULT_SERVER_ADDRESS);

        AuthConfigFile expected = new AuthConfigFile();
        expected.addConfig(authConfig);

        assertEquals(runTest("validLegacy"), expected);
    }

    @Test
    public void nonExistent() throws IOException {
        AuthConfigFile expected = new AuthConfigFile();
        assertEquals(runTest("idontexist"), expected);
    }

    private AuthConfigFile runTest(String testFileName) throws IOException {
        return AuthConfigFile.loadConfig(new File(FILESROOT, testFileName));
    }

}
