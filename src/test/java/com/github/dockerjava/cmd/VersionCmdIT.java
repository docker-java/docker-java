package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Version;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VersionCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(VersionCmdIT.class);

    
    @Test
    public void version() throws DockerException {
        Version version = dockerRule.getClient().versionCmd().exec();
        LOG.info(version.toString());

        assertTrue(version.getGoVersion().length() > 0);
        assertTrue(version.getVersion().length() > 0);

        assertEquals(StringUtils.split(version.getVersion(), ".").length, 3);

    }

}
