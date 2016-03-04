package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.annotations.Test;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class VersionTest {

    @Test
    public void testSerDer1() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().uncheckedSimpleType(Version.class);

        final Version version = testRoundTrip(RemoteApiVersion.VERSION_1_22,
                "/version/1.json",
                type
        );

        assertThat(version, notNullValue());

        assertThat(version.getVersion(), is("1.10.1"));
        assertThat(version.getApiVersion(), is("1.22"));
        assertThat(version.getGitCommit(), is("9e83765"));
        assertThat(version.getGoVersion(), is("go1.5.3"));
        assertThat(version.getOperatingSystem(), is("linux"));
        assertThat(version.getArch(), is("amd64"));
        assertThat(version.getKernelVersion(), is("4.1.17-boot2docker"));
        assertThat(version.getBuildTime(), is("2016-02-11T20:39:58.688092588+00:00"));
    }

}
