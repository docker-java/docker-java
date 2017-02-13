package com.github.dockerjava.core.command;

import java.lang.reflect.Method;

import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;
import com.github.dockerjava.core.DockerClientBuilder;

import static com.github.dockerjava.utils.TestUtils.getVersion;

@Test(groups = "integration")
public class AuthCmdImplTest extends AbstractDockerClientTest {

    @BeforeTest
    public void beforeTest() throws Exception {
        super.beforeTest();
    }

    @AfterTest
    public void afterTest() {
        super.afterTest();
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        super.beforeMethod(method);
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        super.afterMethod(result);
    }

    @Test
    public void testAuth() throws Exception {
        final RemoteApiVersion apiVersion = getVersion(dockerClient);

        if (!apiVersion.isGreaterOrEqual(RemoteApiVersion.VERSION_1_23)) {
            throw new SkipException("Fails on 1.22. Temporary disabled.");
        }

        AuthResponse response = dockerClient.authCmd().exec();

        assertEquals(response.getStatus(), "Login Succeeded");
    }

    // Disabled because of 500/InternalServerException
    @Test(enabled = false, expectedExceptions = UnauthorizedException.class, expectedExceptionsMessageRegExp = "Wrong login/password, please try again")
    public void testAuthInvalid() throws Exception {

        DockerClientBuilder.getInstance(config("garbage")).build().authCmd().exec();
    }
}
