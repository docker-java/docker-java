package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.SearchItem;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ch.lambdaj.Lambda.filter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testinfected.hamcrest.jpa.HasFieldWithValue.hasField;

public class SearchImagesCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(SearchImagesCmdIT.class);

    @Test
    public void searchImages() throws DockerException {
        List<SearchItem> dockerSearch = dockerRule.getClient().searchImagesCmd("busybox").exec();
        LOG.info("Search returned {}", dockerSearch.toString());

        Matcher matcher = hasItem(hasField("name", equalTo("busybox")));
        assertThat(dockerSearch, matcher);

        assertThat(filter(hasField("name", is("busybox")), dockerSearch).size(), equalTo(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void searchImagesWithInvalidMinimumLimit() throws DockerException {
        dockerRule.getClient().searchImagesCmd("busybox").withLimit(0).exec();
    }

    @Test(expected = IllegalArgumentException.class)
    public void searchImagesWithInvalidMaximumLimit() throws DockerException {
        dockerRule.getClient().searchImagesCmd("busybox").withLimit(101).exec();
    }

    @Test
    public void searchImagesWithValidMinimumLimit() throws DockerException {
        List<SearchItem> dockerSearch = dockerRule.getClient().searchImagesCmd("busybox").withLimit(1).exec();
        LOG.info("Search returned {}", dockerSearch.toString());

        Matcher matcher = hasItem(hasField("name", equalTo("busybox")));
        assertThat(dockerSearch, matcher);

        assertThat(filter(hasField("name", is("busybox")), dockerSearch).size(), equalTo(1));

        assertThat(dockerSearch.size(), equalTo(1));
    }

    @Test
    public void searchImagesWithValidMaximumLimit() throws DockerException {
        List<SearchItem> dockerSearch = dockerRule.getClient().searchImagesCmd("busybox").withLimit(1).exec();
        LOG.info("Search returned {}", dockerSearch.toString());

        Matcher matcher = hasItem(hasField("name", equalTo("busybox")));
        assertThat(dockerSearch, matcher);

        assertThat(filter(hasField("name", is("busybox")), dockerSearch).size(), equalTo(1));
    }
}
