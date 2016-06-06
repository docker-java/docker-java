package com.github.dockerjava.api.model;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author Kanstantsin Shautsou
 */
public class DeviceTest {
    @Test
    public void testParse() throws Exception {
        assertThat(Device.parse("/dev/sda:/dev/xvdc:r"),
                equalTo(new Device("r", "/dev/xvdc", "/dev/sda")));

        assertThat(Device.parse("/dev/snd:rw"),
                equalTo(new Device("rw", "/dev/snd", "/dev/snd")));

        assertThat(Device.parse("/dev/snd:/something"),
                equalTo(new Device("rwm", "/something", "/dev/snd")));

        assertThat(Device.parse("/dev/snd:/something:rw"),
                equalTo(new Device("rw", "/something", "/dev/snd")));

    }
}
