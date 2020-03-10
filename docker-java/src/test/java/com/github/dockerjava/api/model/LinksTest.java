package com.github.dockerjava.api.model;

import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class LinksTest {

    @Test
    public void usesToJson() throws Exception {
        Links links = new Links(
                new Link("/foo", "/bar"),
                new Link("bip", "bop")
        );
        String json = JSONTestHelper.getMapper().writeValueAsString(links);

        assertThat(json, is("[\"/foo:/bar\",\"bip:bop\"]"));
    }

    @Test
    public void usesFromJson() throws Exception {
        Links links = JSONTestHelper.getMapper().readValue("[\"/foo:/bar\",\"bip:bop\"]", Links.class);

        assertThat(links, notNullValue());
        assertThat(links.getLinks(), arrayContaining(
                new Link("foo", "bar"),
                new Link("bip", "bop")
        ));
    }

}
