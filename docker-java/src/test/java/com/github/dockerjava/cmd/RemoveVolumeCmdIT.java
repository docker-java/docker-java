package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class RemoveVolumeCmdIT extends CmdIT {

   
    @Test(expected = NotFoundException.class)
    public void removeVolume() throws DockerException {

        String volumeName = "volume1" + dockerRule.getKind();

        CreateVolumeResponse createVolumeResponse = dockerRule.getClient().createVolumeCmd()
                .withName(volumeName)
                .withDriver("local")
                .withLabels(Collections.singletonMap("is-timelord", "yes")).exec();

        assertThat(createVolumeResponse.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse.getLabels(), equalTo(Collections.singletonMap("is-timelord", "yes")));
        assertThat(createVolumeResponse.getMountpoint(), containsString(volumeName));

        dockerRule.getClient().removeVolumeCmd(volumeName).exec();

        dockerRule.getClient().inspectVolumeCmd(volumeName).exec();
    }
}
