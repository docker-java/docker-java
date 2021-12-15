package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.github.dockerjava.api.model.InfoRegistryConfig.IndexConfig;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class InfoTest {

    @Test
    public void serder1Json() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(Info.class);

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

        assertThat(info.getHttpProxy(), is(emptyString()));
        assertThat(info.getHttpsProxy(), is(emptyString()));
        assertThat(info.getNoProxy(), is(emptyString()));
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

    @Test
    public void serder2Json() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(Info.class);

        final Info info = testRoundTrip(VERSION_1_22,
                "info/2.json",
                type
        );

        final List<List<String>> driverStatus = asList(
                asList("Pool Name", "docker-253:2-17567992-pool"),
                asList("Pool Blocksize", "65.54 kB"),
                asList("Base Device Size", "107.4 GB"),
                asList("Backing Filesystem", "ext4"),
                asList("Data file", "/dev/loop0"),
                asList("Metadata file", "/dev/loop1"),
                asList("Data Space Used", "3.89 GB"),
                asList("Data Space Total", "107.4 GB"),
                asList("Data Space Available", "103.5 GB"),
                asList("Metadata Space Used", "5.46 MB"),
                asList("Metadata Space Total", "2.147 GB"),
                asList("Metadata Space Available", "2.142 GB"),
                asList("Udev Sync Supported", "true"),
                asList("Deferred Removal Enabled", "false"),
                asList("Deferred Deletion Enabled", "false"),
                asList("Deferred Deleted Device Count", "0"),
                asList("Data loop file", "/var/lib/docker/devicemapper/devicemapper/data"),
                asList("Metadata loop file", "/var/lib/docker/devicemapper/devicemapper/metadata"),
                asList("Library Version", "1.02.107-RHEL7 (2015-12-01)")
        );

        final Map<String, List<String>> plugins = new LinkedHashMap<>();
        plugins.put("Volume", singletonList("local"));
        plugins.put("Network", asList("null", "host", "bridge"));
        plugins.put("Authorization", null);

        assertThat(info, notNullValue());
        assertThat(info.getArchitecture(), equalTo("x86_64"));
        assertThat(info.getContainersStopped(), is(2));
        assertThat(info.getContainersPaused(), is(0));
        assertThat(info.getContainersRunning(), is(0));
        assertThat(info.getCpuCfsPeriod(), is(true));

        // not available in this dump
        assertThat(info.getCpuCfsQuota(), is(true));
        assertThat(info.getDiscoveryBackend(), nullValue());
        assertThat(info.getOomScoreAdj(), nullValue());

        assertThat(info.getDriverStatuses(), notNullValue());
        assertThat(info.getDriverStatuses(), hasSize(19));
        assertThat(info.getDriverStatuses(), equalTo(driverStatus));

        assertThat(info.getNGoroutines(), is(30));

        assertThat(info.getSystemStatus(), CoreMatchers.nullValue());

        assertThat(info.getPlugins(), equalTo(plugins));
        assertThat(info.getPlugins(), hasEntry("Volume", singletonList("local")));
        assertThat(info.getPlugins(), hasEntry("Authorization", null));

        assertThat(info.getExperimentalBuild(), is(false));

        assertThat(info.getHttpProxy(), is(emptyString()));
        assertThat(info.getHttpsProxy(), is(emptyString()));
        assertThat(info.getNoProxy(), is(emptyString()));
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
        final IndexConfig indexConfig2 = new IndexConfig().withMirrors(Collections.<String>emptyList()).withName("somehost:80")
                .withSecure(false).withOfficial(false);
        assertThat(indexConfigs, hasEntry("somehost:80", indexConfig2));
        assertThat(registryConfig.getMirrors(), nullValue());

        assertThat(info.getSystemTime(), is("2016-03-20T17:32:06.598846244+01:00"));
        assertThat(info.getServerVersion(), is("1.10.2"));

        assertThat(info.getCpuSet(), is(true));
        assertThat(info.getCpuShares(), is(true));
        assertThat(info.getIPv4Forwarding(), is(true));
        assertThat(info.getBridgeNfIptables(), is(false));
        assertThat(info.getBridgeNfIp6tables(), is(false));
        assertThat(info.getDebug(), is(false));
        assertThat(info.getNFd(), is(13));
        assertThat(info.getOomKillDisable(), is(true));
        assertThat(info.getLoggingDriver(), is("json-file"));
        assertThat(info.getOperatingSystem(), is("Red Hat Enterprise Linux Workstation 7.2 (Maipo)"));
        assertThat(info.getClusterStore(), is(""));


        final Info withInfo = new Info().withArchitecture("x86_64")
                .withContainers(2)
                .withContainersRunning(0)
                .withContainersPaused(0)
                .withContainersStopped(2)
                .withImages(55)
                .withId("H52J:52LG:YP4W:EHKY:SRK5:RYG6:ETWR:7AR3:MTFJ:PC6C:4YF2:NTN2")
                .withDriver("devicemapper")
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
                .withBridgeNfIptables(false)
                .withBridgeNfIp6tables(false)
                .withDebug(false)
                .withNFd(13)
                .withOomKillDisable(true)
                .withNGoroutines(30)
                .withSystemTime("2016-03-20T17:32:06.598846244+01:00")
                .withExecutionDriver("native-0.2")
                .withLoggingDriver("json-file")
                .withNEventsListener(0)
                .withKernelVersion("3.10.0-327.10.1.el7.x86_64")
                .withOperatingSystem("Red Hat Enterprise Linux Workstation 7.2 (Maipo)")
                .withOsType("linux")
                .withIndexServerAddress("https://index.docker.io/v1/")
                .withRegistryConfig(registryConfig)
                .withInitSha1("672d65f3cf8816fbda421afeed7e52c0ca17d5e7")
                .withInitPath("/usr/libexec/docker/dockerinit")
                .withNCPU(8)
                .withMemTotal(33350918144L)
                .withDockerRootDir("/var/lib/docker")
                .withHttpProxy("")
                .withHttpsProxy("")
                .withNoProxy("")
                .withName("somename")
                .withLabels(null)
                .withExperimentalBuild(false)
                .withServerVersion("1.10.2")
                .withClusterStore("")
                .withClusterAdvertise("")
                //shredinger-fields
                .withDiscoveryBackend(null)
                .withOomScoreAdj(null)
                .withSockets(null)
                ;

        assertThat(info, is(withInfo));
    }

    @Test
    public void info_1_38() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(Info.class);

        final Info info = testRoundTrip(RemoteApiVersion.VERSION_1_38,
                "info/lcow.json",
                type
        );

        assertThat(info, notNullValue());
        assertThat(info.getArchitecture(), is("x86_64"));
        assertThat(info.getDockerRootDir(), is("C:\\ProgramData\\Docker"));
        assertThat(info.getDriver(), is("windowsfilter (windows) lcow (linux)"));

        assertThat(info.getDriverStatuses(), equalTo(Arrays.asList(
                Arrays.asList("Windows", ""),
                Arrays.asList("LCOW", "")
        )));

        assertThat(info.getIsolation(), is("hyperv"));
        assertThat(info.getKernelVersion(), is("10.0 17134 (17134.1.amd64fre.rs4_release.180410-1804)"));
        assertThat(info.getOsType(), is("windows"));
        assertThat(info.getOperatingSystem(), is("Windows 10 Pro Version 1803 (OS Build 17134.228)"));

        final Map<String, List<String>> plugins = new LinkedHashMap<>();
        plugins.put("Authorization", null);
        plugins.put("Log", asList("awslogs", "etwlogs", "fluentd", "gelf", "json-file", "logentries", "splunk", "syslog"));
        plugins.put("Network", asList("ics", "l2bridge", "l2tunnel", "nat", "null", "overlay", "transparent"));
        plugins.put("Volume", singletonList("local"));
        assertThat(info.getPlugins(), equalTo(plugins));

        assertThat(info.getServerVersion(), is("18.06.1-ce"));
    }
}
