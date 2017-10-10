package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.DockerException;
import org.junit.Test;

public class PingCmdIT extends CmdIT {

    @Test
    public void ping() throws DockerException {
        dockerRule.getClient().pingCmd().exec();
    }

}
