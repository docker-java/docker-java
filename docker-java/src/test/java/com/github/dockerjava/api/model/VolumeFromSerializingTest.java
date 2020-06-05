package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VolumeFromSerializingTest {

    private final String json = "\"container1:ro\"";

    @Test
    public void deserializing() throws Exception {
        VolumesFrom volumeFrom = JSONTestHelper.getMapper().readValue(json, VolumesFrom.class);
        assertEquals(volumeFrom, new VolumesFrom("container1", AccessMode.ro));
    }

    @Test
    public void serializing() throws Exception {
        VolumesFrom volumeFrom = new VolumesFrom("container1", AccessMode.ro);
        assertEquals(JSONTestHelper.getMapper().writeValueAsString(volumeFrom), json);
    }

}
