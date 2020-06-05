package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UlimitsTest {

    @Test
    public void usesToJson() throws Exception {
        Ulimit[] ulimits = new Ulimit[]{
                new Ulimit("nproc", 709, 1026),
                new Ulimit("nofile", 1024, 4096),
                new Ulimit("core", 99999999998L, 99999999999L)
        };
        String json = JSONTestHelper.getMapper().writeValueAsString(ulimits);

        assertThat(json, is("[{\"Name\":\"nproc\",\"Soft\":709,\"Hard\":1026},{\"Name\":\"nofile\",\"Soft\":1024,\"Hard\":4096},{\"Name\":\"core\",\"Soft\":99999999998,\"Hard\":99999999999}]"));
    }

    @Test
    public void usesFromJson() throws Exception {
        Ulimit[] ulimits = JSONTestHelper.getMapper().readValue("[{\"Name\":\"nproc\",\"Soft\":709,\"Hard\":1026},{\"Name\":\"nofile\",\"Soft\":1024,\"Hard\":4096},{\"Name\":\"core\",\"Soft\":99999999998,\"Hard\":99999999999}]", Ulimit[].class);

        assertThat(ulimits, notNullValue());
        assertThat(ulimits, arrayContaining(
                new Ulimit("nproc", 709, 1026),
                new Ulimit("nofile", 1024, 4096),
                new Ulimit("core", 99999999998L, 99999999999L)
        ));
    }
}
