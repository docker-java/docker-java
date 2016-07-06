package com.github.dockerjava.core.command;

import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.client.AbstractDockerClientTest;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
        expectedImageId = "sha256:5d195fbfc7b1ee5cef048b156c2eb9a6e7f00db14420b65d99d0f74a24235764";
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
        final Path imageTarFile = Paths.get("src/test/resources/testLoadImageFromTar/image.tar");

        try (InputStream uploadStream = Files.newInputStream(imageTarFile)) {
            dockerClient.loadImageCmd(uploadStream).exec();
        }

        final Image image = findImageWithId(expectedImageId, dockerClient.listImagesCmd().exec());

        assertNotNull(image, "Can't find expected image after loading from a tar archive");
        assertEquals(image.getRepoTags(), new String[]{"docker-java/load:1.0"},
                "Image after loading from a tar archive has wrong tags!");
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
