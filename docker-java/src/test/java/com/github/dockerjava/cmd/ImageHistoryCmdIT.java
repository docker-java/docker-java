package com.github.dockerjava.cmd;

import com.github.dockerjava.api.model.ImageHistory;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

public class ImageHistoryCmdIT extends CmdIT {

    @Test
    public void imageHistory() {
        List<ImageHistory> history = dockerRule.getClient().imageHistoryCmd("busybox").exec();

        assertThat(history, notNullValue());
        assertThat(history, hasSize(greaterThan(0)));

        ImageHistory entry = history.get(0);
        assertThat(entry.getId(), notNullValue());
        assertThat(entry.getCreated(), notNullValue());
        assertThat(entry.getCreatedBy(), notNullValue());
        assertThat(entry.getSize(), notNullValue());
        assertThat(entry.getComment(), notNullValue());
    }
}
