package com.github.dockerjava.api.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HostConfigTest {

    @Test
    public void testNewObjectsEqual() {
        assertThat(HostConfig.newHostConfig(),
            equalTo(HostConfig.newHostConfig()));
    }
}
