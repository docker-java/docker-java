package com.github.dockerjava.cmd;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class SaveImageCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(SaveImageCmdIT.class);

    @Test
    public void saveImage() throws Exception {

        try (
            InputStream inputStream = dockerRule.getClient().saveImageCmd("busybox").exec();
            InputStream image = IOUtils.toBufferedInputStream(inputStream)
        ) {
            assertThat(image.read(), not(-1));
        }

        try (
            InputStream inputStream = dockerRule.getClient().saveImageCmd("busybox").withTag("latest").exec();
            InputStream image2 = IOUtils.toBufferedInputStream(inputStream)
        ) {
            assertThat(image2.read(), not(-1));
        }
    }

}
