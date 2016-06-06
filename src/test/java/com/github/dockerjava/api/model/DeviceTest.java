package com.github.dockerjava.api.model;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.*;

/**
 * @author Kanstantsin Shautsou
 */
public class DeviceTest {
    //TODO create matcher
    @Test
    public void testParse() throws Exception {
        final Device device = Device.parse("/dev/sda:/dev/xvdc:r");
        assertThat(device.getPathOnHost(), is("/dev/sda"));
        assertThat(device.getPathInContainer(), is("/dev/xvdc"));
        assertThat(device.getcGroupPermissions(), is("r"));
    }

}
