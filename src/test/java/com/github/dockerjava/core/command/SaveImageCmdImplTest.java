package com.github.dockerjava.core.command;

import com.github.dockerjava.api.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;

@Test(groups = "integration")
public class SaveImageCmdImplTest extends AbstractDockerClientTest {
    public static final Logger LOG = LoggerFactory
            .getLogger(PushImageCmdImplTest.class);

    String username;

    @BeforeTest
    public void beforeTest() throws DockerException {
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

        InputStream image = IOUtils.toBufferedInputStream(dockerClient
                .saveImageCmd("busybox").exec());
        assertThat(image.available(), greaterThan(0));

    }

}
