package com.github.dockerjava.cmd;


import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.utils.TestLoadImageResources;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

public class LoadImageTagAsyncCmdIT extends CmdIT {

    private static final Logger log = LoggerFactory.getLogger(LoadImageTagAsyncCmdIT.class);

    private static final String expectedImageTag = "test-image:v1";

    @Before
    public void beforeMethod() {
        if (findImageWithTag(dockerRule.getClient().listImagesCmd().exec()) != null) {
            dockerRule.getClient().removeImageCmd(expectedImageTag).exec();
        }
    }

    @After
    public void afterMethod() {
        dockerRule.getClient().removeImageCmd(expectedImageTag).exec();
    }


    @Test
    public void testLoadImageTagFromTar() throws Exception {
        // return image tag
        log.info("Begin load image from tarball and trying to get imageTag!");
        String loadImageTag;
        try (InputStream uploadStream = Files.newInputStream(TestLoadImageResources.getLoadImageTagTestTarball())) {
            loadImageTag = dockerRule.getClient().loadImageAsyncCmd(uploadStream).start().awaitImageId();
        }
        assertThat("Can't get imageTag after loading from a tar archive!", loadImageTag, notNullValue());

        final Image image = findImageWithTag(dockerRule.getClient().listImagesCmd().exec());

        assertThat("Can't find expected image after loading from a tar archive!", image, notNullValue());
        log.info("The loaded image tag is ==> {}", loadImageTag);
    }



    private Image findImageWithTag(List<Image> images) {
        for (Image image : images) {
            for (String tag : image.getRepoTags()) {
                if (expectedImageTag.equals(tag)) {
                    return image;
                }
            }
        }
        return null;
    }
}
