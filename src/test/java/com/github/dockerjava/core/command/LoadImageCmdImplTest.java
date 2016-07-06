package com.github.dockerjava.core.command;

import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.client.AbstractDockerClientTest;

import com.github.dockerjava.utils.TestResources;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

@Test(groups = "integration")
public class LoadImageCmdImplTest extends AbstractDockerClientTest {

    private String expectedImageId;

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
        expectedImageId = "sha256:56031f66eb0cef2e2e5cb2d1dabafaa0ebcd0a18a507d313b5bdb8c0472c5eba";
        if (findImageWithId(expectedImageId, dockerClient.listImagesCmd().exec()) != null) {
            dockerClient.removeImageCmd(expectedImageId).exec();
        }
    }

    @AfterMethod
    public void afterMethod(ITestResult result) {
        dockerClient.removeImageCmd(expectedImageId).exec();
        super.afterMethod(result);
    }

    @Test
    public void loadImageFromTar() throws Exception {
        try (InputStream uploadStream = Files.newInputStream(TestResources.getApiImagesLoadTestTarball())) {
            dockerClient.loadImageCmd(uploadStream).exec();
        }

        final Image image = findImageWithId(expectedImageId, dockerClient.listImagesCmd().exec());

        assertThat("Can't find expected image after loading from a tar archive!", image, notNullValue());
        assertThat("Image after loading from a tar archive has wrong tags!",
                asList(image.getRepoTags()), equalTo(singletonList("docker-java/load:1.0")));
    }

    private Image findImageWithId(final String id, final List<Image> images) {
        for (Image image : images) {
            if (id.equals(image.getId())) {
                return image;
            }
        }
        return null;
    }
}
