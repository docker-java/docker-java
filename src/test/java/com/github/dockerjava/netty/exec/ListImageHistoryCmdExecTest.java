package com.github.dockerjava.netty.exec;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.History;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.netty.AbstractNettyDockerClientTest;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class ListImageHistoryCmdExecTest extends AbstractNettyDockerClientTest {

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
    public void listImageHistory() throws DockerException {
        List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();
        assertThat(images, notNullValue());
        assertThat(images, hasSize(greaterThanOrEqualTo(1)));
        LOG.info("Images List: {}", images);

        String imageId = images.get(0).getId();

        List<History> history = dockerClient.listImageHistoryCmd(imageId).exec();

        assertThat(history, notNullValue());
    }

}
