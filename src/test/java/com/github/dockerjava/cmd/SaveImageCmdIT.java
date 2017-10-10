package com.github.dockerjava.cmd;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

public class SaveImageCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(SaveImageCmdIT.class);

    @Test
    public void saveImage() throws Exception {

        InputStream image = IOUtils.toBufferedInputStream(dockerRule.getClient().saveImageCmd("busybox").exec());
        assertThat(image.available(), greaterThan(0));

    }

}
