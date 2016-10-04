package com.github.dockerjava.core.command;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.History;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.client.AbstractDockerClientTest;
import org.hamcrest.Matchers;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Test(groups = "integration")
public class ListImageHistoryCmdImplTest extends AbstractDockerClientTest {

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
    public void testListHistory() throws Exception {

        List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();
        assertThat(images, notNullValue());
        assertThat(images, Matchers.hasSize(Matchers.greaterThanOrEqualTo(1)));
        LOG.info("Images List: {}", images);

        String imageId = images.get(0).getId();

        List<History> history = dockerClient.listImageHistoryCmd(imageId).exec();

        assertThat(history, notNullValue());
    }

    @Test(expectedExceptions = NotFoundException.class)
    public void testListHistoryOfNonExistingImage(){

        List<History> history = dockerClient.listImageHistoryCmd("non-existing").exec();
    }
}
