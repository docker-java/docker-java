package com.github.dockerjava.netty.exec;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class AuthCmdExecTest extends AbstractNettyDockerClientTest {

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
        AuthResponse response = dockerClient.authCmd().exec();

        assertEquals(response.getStatus(), "Login Succeeded");
    }

    @Test(expectedExceptions = UnauthorizedException.class)
    public void testAuthInvalid() throws Exception {
        DockerClientBuilder.getInstance(config("garbage")).build().authCmd().exec();
    }
}
