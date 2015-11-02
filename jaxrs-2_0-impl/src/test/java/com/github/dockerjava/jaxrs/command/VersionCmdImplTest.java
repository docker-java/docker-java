package com.github.dockerjava.jaxrs.command;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.jaxrs.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class VersionCmdImplTest extends AbstractDockerClientTest {

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
    public void version() throws DockerException {
        Version version = dockerClient.versionCmd().exec();
        AbstractDockerClientTest.LOG.info(version.toString());

        Assert.assertTrue(version.getGoVersion().length() > 0);
        Assert.assertTrue(version.getVersion().length() > 0);

        Assert.assertEquals(StringUtils.split(version.getVersion(), ".").length, 3);

    }

}
