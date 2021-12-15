package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.InspectVolumeResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class InspectVolumeCmdIT extends CmdIT {

    @Test
    public void inspectVolume() throws DockerException {

        String volumeName = "volume1";

        dockerRule.getClient().createVolumeCmd().withName(volumeName).withDriver("local")
                .withLabels(Collections.singletonMap("is-timelord", "yes")).exec();

        InspectVolumeResponse inspectVolumeResponse = dockerRule.getClient().inspectVolumeCmd(volumeName).exec();

        assertThat(inspectVolumeResponse.getName(), equalTo("volume1"));
        assertThat(inspectVolumeResponse.getDriver(), equalTo("local"));
        assertThat(inspectVolumeResponse.getLabels(), equalTo(Collections.singletonMap("is-timelord", "yes")));
        assertThat(inspectVolumeResponse.getMountpoint(), containsString("/volume1/"));
    }

    @Test(expected = NotFoundException.class)
    public void inspectNonExistentVolume() throws DockerException {

        String volumeName = "non-existing";

        dockerRule.getClient().inspectVolumeCmd(volumeName).exec();
    }
}
