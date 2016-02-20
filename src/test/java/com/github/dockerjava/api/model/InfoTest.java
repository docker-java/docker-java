package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.InfoRegistryConfig.IndexConfig;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class InfoTest {

    @Test
    public void serder1Json() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().constructType(Info.class);

        final Info info = testRoundTrip(VERSION_1_22,
                "info/1.json",
                type
        );

        assertThat(info, notNullValue());
        assertThat(info.getArchitecture(), equalTo("x86_64"));
        assertThat(info.getContainersStopped(), is(3));
        assertThat(info.getContainersPaused(), is(10));
        assertThat(info.getContainersRunning(), is(2));
        assertThat(info.getCpuCfsPeriod(), is(true));

        // not available in this dump
        assertThat(info.getCpuCfsQuota(), nullValue());
        assertThat(info.getDiscoveryBackend(), nullValue());
        assertThat(info.getOomScoreAdj(), nullValue());

        assertThat(info.getDriverStatuses(), notNullValue());
        assertThat(info.getDriverStatuses(), hasSize(4));

        assertThat(info.getNGoroutines(), is(40));

        assertThat(info.getSystemStatus(), CoreMatchers.nullValue());

        assertThat(info.getPlugins(), hasEntry("Volume", Collections.singletonList("local")));
        assertThat(info.getPlugins(), hasEntry("Authorization", null));

        assertThat(info.getExperimentalBuild(), is(false));

        assertThat(info.getHttpProxy(), isEmptyString());
        assertThat(info.getHttpsProxy(), isEmptyString());
        assertThat(info.getNoProxy(), isEmptyString());
        assertThat(info.getOomKillDisable(), is(true));
        assertThat(info.getOsType(), equalTo("linux"));

        final InfoRegistryConfig registryConfig = info.getRegistryConfig();
        assertThat(registryConfig, notNullValue());
        final List<String> cidRs = registryConfig.getInsecureRegistryCIDRs();
        assertThat(cidRs, notNullValue());
        assertThat(cidRs, contains("127.0.0.0/8"));

        final Map<String, IndexConfig> indexConfigs = registryConfig.getIndexConfigs();
        assertThat(indexConfigs, notNullValue());
        final IndexConfig indexConfig = new IndexConfig().withMirrors(null).withName("docker.io")
                .withSecure(true).withOfficial(true);
        assertThat(indexConfigs, hasEntry("docker.io", indexConfig));
        assertThat(registryConfig.getMirrors(), nullValue());

        assertThat(info.getSystemTime(), equalTo("2016-02-17T14:56:35.212841831Z"));
        assertThat(info.getServerVersion(), equalTo("1.10.1"));
    }
}
