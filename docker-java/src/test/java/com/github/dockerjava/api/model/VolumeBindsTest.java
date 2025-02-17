package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class VolumeBindsTest {

    @Test
    public void usesToJson() throws Exception {
        VolumeBinds binds = new VolumeBinds(
            new VolumeBind("/bar", "/foo"),
            new VolumeBind("/bop", "/bip")
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(binds);

        assertThat(json, is("{\"/foo\":\"/bar\",\"/bip\":\"/bop\"}"));
    }

    @Test
    public void t() throws IOException {
        String s = "{\"/data\":\"/some/path\"}";
        VolumeBinds volumeBinds = JSONTestHelper.getMapper().readValue(s, VolumeBinds.class);
        VolumeBind[] binds = volumeBinds.getBinds();
        assertEquals(1, binds.length);
        assertEquals("/some/path", binds[0].getHostPath());
        assertEquals("/data", binds[0].getContainerPath());
    }

    @Test(expected = JsonMappingException.class)
    public void t1() throws IOException {
        String s = "{\"/data\": {} }";
        JSONTestHelper.getMapper().readValue(s, VolumeBinds.class);
    }

}
