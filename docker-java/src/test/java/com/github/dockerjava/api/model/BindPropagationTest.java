package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class BindPropagationTest {

    @Test
    public void toJson() throws Exception {
        String value = JSONTestHelper.getMapper().writeValueAsString(BindPropagation.R_PRIVATE);

        assertThat(value, is("\"rprivate\""));
    }

    @Test
    public void fromJson() throws Exception {
        BindPropagation value = JSONTestHelper.getMapper().readValue("\"rprivate\"", BindPropagation.class);

        assertThat(value, is(BindPropagation.R_PRIVATE));
    }
}
