package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class BindsTest {

    @Test
    public void usesToJson() throws Exception {
        Binds binds = new Binds(
                Bind.parse("/foo:/bar:rw"),
                Bind.parse("/bip:/bop:ro")
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(binds);

        assertThat(json, is("[\"/foo:/bar:rw\",\"/bip:/bop:ro\"]"));
    }

    @Test
    public void usesFromJson() throws Exception {
        Binds binds = JSONTestHelper.getMapper().readValue("[\"/foo:/bar:rw\",\"/bip:/bop:ro\"]", Binds.class);

        assertThat(binds, notNullValue());
        assertThat(binds.getBinds(), arrayContaining(
                Bind.parse("/foo:/bar:rw"),
                Bind.parse("/bip:/bop:ro")
        ));
    }
}
