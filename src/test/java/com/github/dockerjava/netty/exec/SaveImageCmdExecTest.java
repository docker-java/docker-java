package com.github.dockerjava.netty.exec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.netty.AbstractNettyDockerClientTest;

@Test(groups = "integration")
public class SaveImageCmdExecTest extends AbstractNettyDockerClientTest {
    public static final Logger LOG = LoggerFactory.getLogger(SaveImageCmdExecTest.class);

    String username;

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
    public void saveImage() throws Exception {

        try (InputStream image = dockerClient.saveImageCmd("busybox").exec()) {
            assertThat(image.available(), greaterThan(0));
        }

    }

}
