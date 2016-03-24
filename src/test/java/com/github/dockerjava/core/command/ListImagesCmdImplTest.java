package com.github.dockerjava.core.command;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

import java.lang.reflect.Method;
import java.util.List;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.client.AbstractDockerClientTest;

@Test(groups = "integration")
public class ListImagesCmdImplTest extends AbstractDockerClientTest {

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
    public void listImages() throws DockerException {
        List<Image> images = dockerClient.listImagesCmd().withShowAll(true).exec();
        assertThat(images, notNullValue());
        LOG.info("Images List: {}", images);
        Info info = dockerClient.infoCmd().exec();

        assertThat(images.size(), equalTo(info.getImages()));

        Image img = images.get(0);
        assertThat(img.getCreated(), is(greaterThan(0L)));
        assertThat(img.getVirtualSize(), is(greaterThan(0L)));
        assertThat(img.getId(), not(isEmptyString()));
        assertThat(img.getRepoTags(), not(emptyArray()));
    }

    @Test(groups = "ignoreInCircleCi")
    public void listImagesWithDanglingFilter() throws DockerException {
        String imageId = createDanglingImage();
        List<Image> images = dockerClient.listImagesCmd().withDanglingFilter(true).withShowAll(true)
                .exec();
        assertThat(images, notNullValue());
        LOG.info("Images List: {}", images);
        assertThat(images.size(), is(greaterThan(0)));
        Boolean imageInFilteredList = isImageInFilteredList(images, imageId);
        assertTrue(imageInFilteredList);
    }

    private boolean isImageInFilteredList(List<Image> images, String expectedImageId) {
        for (Image image : images) {
            if (expectedImageId.equals(image.getId())) {
                return true;
            }
        }
        return false;
    }

    private String createDanglingImage() {
        CreateContainerResponse container = dockerClient.createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(isEmptyString()));
        dockerClient.startContainerCmd(container.getId()).exec();

        LOG.info("Committing container {}", container.toString());
        String imageId = dockerClient.commitCmd(container.getId()).exec();

        dockerClient.stopContainerCmd(container.getId()).exec();
        dockerClient.removeContainerCmd(container.getId()).exec();
        return imageId;
    }
}
