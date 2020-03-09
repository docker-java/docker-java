package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class VolumesTest {

    @Test
    public void usesToJson() throws Exception {
        Volumes volumes = new Volumes(
                new Volume("/foo"),
                new Volume("/bar")
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(volumes);

        assertThat(json, is("{\"/foo\":{},\"/bar\":{}}"));
    }

    @Test
    public void usesFromJson() throws Exception {
        Volumes volumes = JSONTestHelper.getMapper().readValue("{\"/foo\":{},\"/bar\":{}}", Volumes.class);

        assertThat(volumes, notNullValue());
        assertThat(volumes.getVolumes(), arrayContaining(
                new Volume("/foo"),
                new Volume("/bar")
        ));
    }
}
