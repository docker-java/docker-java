package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.NotFoundException;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagImageCmdTest extends CmdTest {

    public static final Logger LOG = LoggerFactory.getLogger(TagImageCmdTest.class);

    @Test
    public void tagImage() throws Exception {
        String tag = "" + RandomUtils.nextInt(Integer.MAX_VALUE);

        dockerRule.getClient().tagImageCmd("busybox:latest", "docker-java/busybox", tag).exec();

        dockerRule.getClient().removeImageCmd("docker-java/busybox:" + tag).exec();
    }

    @Test(expected = NotFoundException.class)
    public void tagNonExistingImage() throws Exception {

        String tag = "" + RandomUtils.nextInt(Integer.MAX_VALUE);
        dockerRule.getClient().tagImageCmd("non-existing", "docker-java/busybox", tag).exec();
    }

}
