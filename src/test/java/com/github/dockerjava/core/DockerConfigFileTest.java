/**
 * Copyright (C) 2014 SignalFuse, Inc.
 */
package com.github.dockerjava.core;

import com.github.dockerjava.api.model.AuthConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class DockerConfigFileTest {

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

        DockerConfigFile expected = new DockerConfigFile();
        expected.addAuthConfig(authConfig1);
        expected.addAuthConfig(authConfig2);

        Assert.assertEquals(runTest("validJson"), expected);

    }

    @Test
    public void validLegacy() throws IOException {
        AuthConfig authConfig = new AuthConfig()
                .withEmail("foo@example.com")
                .withUsername("foo")
                .withPassword("bar")
                .withRegistryAddress(AuthConfig.DEFAULT_SERVER_ADDRESS);

        DockerConfigFile expected = new DockerConfigFile();
        expected.addAuthConfig(authConfig);

        Assert.assertEquals(runTest("validLegacy"), expected);
    }

    @Test
    public void validDockerConfig() throws IOException {
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

        DockerConfigFile expected = new DockerConfigFile();
        expected.addAuthConfig(authConfig1);
        expected.addAuthConfig(authConfig2);

        Assert.assertEquals(runTest("validDockerConfig"), expected);
    }

    @Test
    public void nonExistent() throws IOException {
        DockerConfigFile expected = new DockerConfigFile();
        Assert.assertEquals(runTest("idontexist"), expected);
    }

    private DockerConfigFile runTest(String testFileName) throws IOException {
        return DockerConfigFile.loadConfig(new File(FILESROOT, testFileName));
    }

}
