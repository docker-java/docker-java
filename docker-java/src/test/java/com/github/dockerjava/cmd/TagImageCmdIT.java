package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.NotFoundException;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagImageCmdIT extends CmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(TagImageCmdIT.class);

    @Test
    public void tagImage() {
        String tag = "" + RandomUtils.nextInt(0, Integer.MAX_VALUE);

        dockerRule.getClient().tagImageCmd("busybox:latest", "docker-java/busybox", tag).exec();

        dockerRule.getClient().removeImageCmd("docker-java/busybox:" + tag).exec();
    }

    @Test(expected = NotFoundException.class)
    public void tagNonExistingImage() {

        String tag = "" + RandomUtils.nextInt(0, Integer.MAX_VALUE);
        dockerRule.getClient().tagImageCmd("non-existing", "docker-java/busybox", tag).exec();
    }

}
