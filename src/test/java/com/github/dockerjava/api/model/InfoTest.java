package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.model.InfoRegistryConfig.IndexConfig;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
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

        final List<List<String>> driverStatus = asList(
                asList("Root Dir", "/mnt/sda1/var/lib/docker/aufs"),
                asList("Backing Filesystem", "extfs"),
                asList("Dirs", "31"),
                asList("Dirperm1 Supported", "true")
        );

        final Map<String, List<String>> plugins = new LinkedHashMap<>();
        plugins.put("Volume", singletonList("local"));
        plugins.put("Network", asList("bridge", "null", "host"));
        plugins.put("Authorization", null);

        assertThat(info, notNullValue());
        assertThat(info.getArchitecture(), equalTo("x86_64"));
        assertThat(info.getContainersStopped(), is(3));
        assertThat(info.getContainersPaused(), is(10));
        assertThat(info.getContainersRunning(), is(2));
        assertThat(info.getCpuCfsPeriod(), is(true));

        // not available in this dump
        assertThat(info.getCpuCfsQuota(), is(true));
        assertThat(info.getDiscoveryBackend(), nullValue());
        assertThat(info.getOomScoreAdj(), nullValue());

        assertThat(info.getDriverStatuses(), notNullValue());
        assertThat(info.getDriverStatuses(), hasSize(4));
        assertThat(info.getDriverStatuses(), equalTo(driverStatus));

        assertThat(info.getNGoroutines(), is(40));

        assertThat(info.getSystemStatus(), CoreMatchers.nullValue());

        assertThat(info.getPlugins(), equalTo(plugins));
        assertThat(info.getPlugins(), hasEntry("Volume", singletonList("local")));
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

        assertThat(info.getSystemTime(), is("2016-02-17T14:56:35.212841831Z"));
        assertThat(info.getServerVersion(), is("1.10.1"));

        assertThat(info.getCpuSet(), is(true));
        assertThat(info.getCpuShares(), is(true));
        assertThat(info.getIPv4Forwarding(), is(true));
        assertThat(info.getBridgeNfIptables(), is(true));
        assertThat(info.getBridgeNfIp6tables(), is(true));
        assertThat(info.getDebug(), is(true));
        assertThat(info.getNFd(), is(24));
        assertThat(info.getOomKillDisable(), is(true));
        assertThat(info.getLoggingDriver(), is("json-file"));
        assertThat(info.getOperatingSystem(),
                is("Boot2Docker 1.10.1 (TCL 6.4.1); master : b03e158 - Thu Feb 11 22:34:01 UTC 2016"));
        assertThat(info.getClusterStore(), is(""));


        final Info withInfo = new Info().withArchitecture("x86_64")
                .withContainers(2)
                .withContainersRunning(2)
                .withContainersPaused(10)
                .withContainersStopped(3)
                .withImages(13)
                .withId("HLN2:5SBU:SRQR:CQI6:AB52:LZZ2:DED5:REDM:BU73:JFHE:R37A:5HMX")
                .withDriver("aufs")
                .withDriverStatuses(driverStatus)
                .withSystemStatus(null)
                .withPlugins(plugins)
                .withMemoryLimit(true)
                .withSwapLimit(true)
                .withCpuCfsPeriod(true)
                .withCpuCfsQuota(true)
                .withCpuShares(true)
                .withCpuSet(true)
                .withIPv4Forwarding(true)
                .withBridgeNfIptables(true)
                .withBridgeNfIp6tables(true)
                .withDebug(true)
                .withNFd(24)
                .withOomKillDisable(true)
                .withNGoroutines(40)
                .withSystemTime("2016-02-17T14:56:35.212841831Z")
                .withExecutionDriver("native-0.2")
                .withLoggingDriver("json-file")
                .withNEventsListener(0)
                .withKernelVersion("4.1.17-boot2docker")
                .withOperatingSystem("Boot2Docker 1.10.1 (TCL 6.4.1); master : b03e158 - Thu Feb 11 22:34:01 UTC 2016")
                .withOsType("linux")
                .withIndexServerAddress("https://index.docker.io/v1/")
                .withRegistryConfig(registryConfig)
                .withInitSha1("")
                .withInitPath("/usr/local/bin/docker")
                .withNCPU(1)
                .withMemTotal(1044574208L)
                .withDockerRootDir("/mnt/sda1/var/lib/docker")
                .withHttpProxy("")
                .withHttpsProxy("")
                .withNoProxy("")
                .withName("docker-java")
                .withLabels(new String[]{"provider=virtualbox"})
                .withExperimentalBuild(false)
                .withServerVersion("1.10.1")
                .withClusterStore("")
                .withClusterAdvertise("")
                //shredinger-fields
                .withDiscoveryBackend(null)
                .withOomScoreAdj(null)
                .withSockets(null)
                ;

        assertThat(info, is(withInfo));
    }
}
