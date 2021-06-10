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

public class LoadImageIdAsyncCmdIT extends CmdIT {

    private static final Logger log = LoggerFactory.getLogger(LoadImageIdAsyncCmdIT.class);

    private static final String expectedImageId = "sha256:4167935cbd8ffa5b3cf2248fd4e2943a374923adbd55597fe79eebaec24005b2";

    @Before
    public void beforeMethod() {
        if (findImageWithId(dockerRule.getClient().listImagesCmd().exec()) != null) {
            dockerRule.getClient().removeImageCmd(expectedImageId).exec();
        }
    }

    @After
    public void afterMethod() {
        dockerRule.getClient().removeImageCmd(expectedImageId).exec();
    }

    @Test
    public void testLoadImageIdFromTar() throws Exception {
        // return image id
        log.info("Begin load image from tarball and trying to get imageId!");
        String loadImageId;
        try (InputStream uploadStream = Files.newInputStream(TestLoadImageResources.getLoadImageIdTestTarball())) {
            loadImageId = dockerRule.getClient().loadImageAsyncCmd(uploadStream).start().awaitImageId();
        }
        assertThat("Can't get imageId after loading from a tar archive!", loadImageId, notNullValue());

        final Image image = findImageWithId(dockerRule.getClient().listImagesCmd().exec());

        assertThat("Can't find expected image after loading from a tar archive!", image, notNullValue());
        log.info("The loaded image id is ==> {}", loadImageId);
    }


    private Image findImageWithId(List<Image> images) {
        for (Image image : images) {
            if (expectedImageId.equals(image.getId())) {
                return image;
            }
        }
        return null;
    }

}
