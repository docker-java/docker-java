package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateVolumeResponse;
import com.github.dockerjava.api.command.ListVolumesResponse;
import com.github.dockerjava.api.exception.DockerException;
import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class ListVolumesCmdIT extends CmdIT {
   
    @Test
    public void listVolumes() throws DockerException {

        CreateVolumeResponse createVolumeResponse = dockerRule.getClient().createVolumeCmd().withName("volume1")
                .withDriver("local").withLabels(Collections.singletonMap("is-timelord", "yes")).exec();

        assertThat(createVolumeResponse.getName(), equalTo("volume1"));
        assertThat(createVolumeResponse.getDriver(), equalTo("local"));
        assertThat(createVolumeResponse.getLabels(), equalTo(Collections.singletonMap("is-timelord", "yes")));
        assertThat(createVolumeResponse.getMountpoint(), containsString("/volume1/"));

        ListVolumesResponse listVolumesResponse = dockerRule.getClient().listVolumesCmd().exec();

        assertThat(listVolumesResponse.getVolumes().size(), greaterThanOrEqualTo(1));
    }
}
