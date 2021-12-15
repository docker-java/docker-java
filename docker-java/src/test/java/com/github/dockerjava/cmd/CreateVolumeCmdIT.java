package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.exception.DockerException;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class CreateVolumeCmdIT extends CmdIT {

    @Test
    public void createVolume() throws DockerException {

        String volumeName = "volume1";

        CreateVolumeResponse createVolumeResponse = dockerRule.getClient().createVolumeCmd().withName(volumeName)
                .withDriver("local").withLabels(Collections.singletonMap("is-timelord", "yes")).exec();

        assertThat(createVolumeResponse.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse.getLabels(), equalTo(Collections.singletonMap("is-timelord", "yes")));
        assertThat(createVolumeResponse.getMountpoint(), containsString("/volume1/"));
    }

    @Test
    public void createVolumeWithExistingName() throws DockerException {

        String volumeName = "volume1";

        CreateVolumeResponse createVolumeResponse1 = dockerRule.getClient().createVolumeCmd().withName(volumeName)
                .withDriver("local").withLabels(Collections.singletonMap("is-timelord", "yes")).exec();

        assertThat(createVolumeResponse1.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse1.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse1.getLabels(), equalTo(Collections.singletonMap("is-timelord", "yes")));
        assertThat(createVolumeResponse1.getMountpoint(), containsString("/volume1/"));

        CreateVolumeResponse createVolumeResponse2 = dockerRule.getClient().createVolumeCmd().withName(volumeName)
                .withDriver("local").withLabels(Collections.singletonMap("is-timelord", "yes")).exec();

        assertThat(createVolumeResponse2.getName(), equalTo(volumeName));
        assertThat(createVolumeResponse2.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse2.getLabels(), equalTo(Collections.singletonMap("is-timelord", "yes")));
        assertThat(createVolumeResponse2.getMountpoint(), equalTo(createVolumeResponse1.getMountpoint()));
    }
}
