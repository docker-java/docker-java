package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class VersionTest {

    @Test
    public void testSerDer1() throws Exception {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(Version.class);

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

    @Test
    public void version_1_38() throws Exception {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(Version.class);

        final Version version = testRoundTrip(RemoteApiVersion.VERSION_1_38,
                "/version/lcow.json",
                type
        );

        assertThat(version, notNullValue());
        assertThat(version.getApiVersion(), is("1.38"));
        assertThat(version.getArch(), is("amd64"));
        assertThat(version.getBuildTime(), is("2018-08-21T17:36:40.000000000+00:00"));

        Map<String, String> details = new LinkedHashMap<>();
        details.put("ApiVersion", "1.38");
        details.put("Arch", "amd64");
        details.put("BuildTime", "2018-08-21T17:36:40.000000000+00:00");
        details.put("Experimental", "true");
        details.put("GitCommit", "e68fc7a");
        details.put("GoVersion", "go1.10.3");
        details.put("KernelVersion", "10.0 17134 (17134.1.amd64fre.rs4_release.180410-1804)");
        details.put("MinAPIVersion", "1.24");
        details.put("Os", "windows");

        List<VersionComponent> components = Collections.singletonList(new VersionComponent()
                .withDetails(details)
                .withName("Engine")
                .withVersion("18.06.1-ce")
        );
        assertThat(version.getComponents(), equalTo(components));

        assertThat(version.getExperimental(), is(true));
        assertThat(version.getGitCommit(), is("e68fc7a"));
        assertThat(version.getGoVersion(), is("go1.10.3"));
        assertThat(version.getKernelVersion(), is("10.0 17134 (17134.1.amd64fre.rs4_release.180410-1804)"));
        assertThat(version.getMinAPIVersion(), is("1.24"));
        assertThat(version.getOperatingSystem(), is("windows"));
        assertThat(version.getPlatform(), equalTo(new VersionPlatform().withName("")));
        assertThat(version.getVersion(), is("18.06.1-ce"));
    }
}
