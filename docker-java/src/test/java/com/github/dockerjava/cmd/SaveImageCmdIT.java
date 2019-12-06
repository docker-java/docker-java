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

        InputStream image = IOUtils.toBufferedInputStream(dockerRule.getClient().saveImageCmd("busybox").exec());
        assertThat(image.read(), not(-1));

        InputStream image2 = IOUtils.toBufferedInputStream(dockerRule.getClient().saveImageCmd("busybox").withTag("latest").exec());
        assertThat(image2.read(), not(-1));


    }

}
