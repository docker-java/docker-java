package com.github.dockerjava.cmd;

import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.utils.TestResources;
import net.jcip.annotations.NotThreadSafe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@NotThreadSafe
public class LoadImageCmdIT extends CmdIT {

    private String expectedImageId;
    
    @Before
    public void beforeMethod() {
        expectedImageId = "sha256:56031f66eb0cef2e2e5cb2d1dabafaa0ebcd0a18a507d313b5bdb8c0472c5eba";
        if (findImageWithId(expectedImageId, dockerRule.getClient().listImagesCmd().exec()) != null) {
            dockerRule.getClient().removeImageCmd(expectedImageId).exec();
        }
    }

    @After
    public void afterMethod() {
        dockerRule.getClient().removeImageCmd(expectedImageId).exec();
    }

    @Test
    public void loadImageFromTar() throws Exception {
        try (InputStream uploadStream = Files.newInputStream(TestResources.getApiImagesLoadTestTarball())) {
            dockerRule.getClient().loadImageCmd(uploadStream).exec();
        }

        //swarm needs some time to refelct new images
        synchronized (this) {
            wait(5000);
        }

        final Image image = findImageWithId(expectedImageId, dockerRule.getClient().listImagesCmd().exec());

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
