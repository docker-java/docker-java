package com.github.dockerjava.jaxrs.jersey.client.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.jaxrs.jersey.client.client.AbstractDockerClientTest;
import org.apache.commons.lang.StringUtils;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

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

        assertTrue(version.getGoVersion().length() > 0);
        assertTrue(version.getVersion().length() > 0);

        assertEquals(StringUtils.split(version.getVersion(), ".").length, 3);

    }

}
