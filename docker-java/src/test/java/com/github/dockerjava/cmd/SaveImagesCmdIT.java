package com.github.dockerjava.cmd;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class SaveImagesCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(SaveImagesCmdIT.class);

    @Test
    public void saveNoImages() throws Exception {
        try(final InputStream image = IOUtils.toBufferedInputStream(dockerRule.getClient().saveImagesCmd().exec())){
            assertThat(image.read(), not(-1));
        }

    }

    @Test
    public void saveImagesWithNameAndTag() throws Exception {

        try(final InputStream image = IOUtils.toBufferedInputStream(dockerRule.getClient().saveImagesCmd().withImage("busybox", "latest").exec())) {
            assertThat(image.read(), not(-1));
        }

    }

    @Test
    public void saveMultipleImages() throws Exception {

        try(final InputStream image = IOUtils.toBufferedInputStream(dockerRule.getClient().saveImagesCmd()
            // Not a real life use-case but "busybox" is the only one I dare to assume is really there.
            .withImage("busybox", "latest")
            .withImage("busybox", "latest")
            .exec())) {
            assertThat(image.read(), not(-1));
        }
    }

}
