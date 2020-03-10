package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class VolumesRWTest {

    @Test
    public void usesToJson() throws Exception {
        VolumesRW volumes = new VolumesRW(
                new VolumeRW(new Volume("/foo")),
                new VolumeRW(new Volume("/bar"), AccessMode.ro)
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(volumes);

        assertThat(json, is("{\"/foo\":true,\"/bar\":false}"));
    }

    @Test
    public void usesFromJson() throws Exception {
        VolumesRW volumes = JSONTestHelper.getMapper().readValue("{\"/foo\":true,\"/bar\":false}", VolumesRW.class);

        assertThat(volumes, notNullValue());
        assertThat(volumes.getVolumesRW(), arrayContaining(
                new VolumeRW(new Volume("/foo")),
                new VolumeRW(new Volume("/bar"), AccessMode.ro)
        ));
    }

}
