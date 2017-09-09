package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RemoveVolumeCmdTest extends CmdTest {

   
    @Test(expected = NotFoundException.class)
    public void removeVolume() throws DockerException {

        String volumeName = "volume1";

        CreateVolumeResponse createVolumeResponse = dockerRule.getClient().createVolumeCmd().withName(volumeName)
                .withDriver("local").exec();

        assertThat(createVolumeResponse.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse.getMountpoint(), containsString("/volume1/"));

        dockerRule.getClient().removeVolumeCmd(volumeName).exec();

        dockerRule.getClient().inspectVolumeCmd(volumeName).exec();
    }
}
