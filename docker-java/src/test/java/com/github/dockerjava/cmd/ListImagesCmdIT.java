package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.github.dockerjava.utils.TestUtils.isNotSwarm;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class ListImagesCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(ListImagesCmdIT.class);

    @Test
    public void listImages() throws DockerException {
        List<Image> images = dockerRule.getClient().listImagesCmd().withShowAll(true).exec();
        assertThat(images, notNullValue());
        LOG.info("Images List: {}", images);
        Info info = dockerRule.getClient().infoCmd().exec();

        if (isNotSwarm(dockerRule.getClient())) {
            assertThat(images.size(), equalTo(info.getImages()));
        }

        Image img = images.get(0);
        assertThat(img.getCreated(), is(greaterThan(0L)));
        assertThat(img.getVirtualSize(), is(greaterThan(0L)));
        assertThat(img.getId(), not(is(emptyString())));
        assertThat(img.getRepoTags(), not(emptyArray()));
    }

    @Test
    public void listImagesWithDanglingFilter() throws DockerException {
        String imageId = createDanglingImage();
        List<Image> images = dockerRule.getClient().listImagesCmd().withDanglingFilter(true).withShowAll(true)
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
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd("busybox").withCmd("sleep", "9999").exec();
        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));
        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        LOG.info("Committing container {}", container.toString());
        String imageId = dockerRule.getClient().commitCmd(container.getId()).exec();

        dockerRule.getClient().stopContainerCmd(container.getId()).exec();
        dockerRule.getClient().removeContainerCmd(container.getId()).exec();
        return imageId;
    }
}
