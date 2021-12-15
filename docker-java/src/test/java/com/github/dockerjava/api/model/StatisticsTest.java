package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;

/**
 * @author Yuting Liu
 */
public class StatisticsTest {

    @Test
    public void serderJson1() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(Statistics.class);

        final Statistics statistics = testRoundTrip(RemoteApiVersion.VERSION_1_27,
                "containers/container/stats/stats1.json",
                type
        );

        assertThat(statistics.getRead(), equalTo("2017-12-06T00:42:03.8352972Z"));

        final StatisticNetworksConfig network = statistics.getNetworks().get("eth0");
        assertThat(network.getRxBytes(), is(1230L));
        assertThat(network.getRxPackets(), is(19L));
        assertThat(network.getRxErrors(), is(0L));
        assertThat(network.getRxDropped(), is(0L));
        assertThat(network.getTxBytes(), is(0L));
        assertThat(network.getTxPackets(), is(0L));
        assertThat(network.getTxErrors(), is(0L));
        assertThat(network.getTxDropped(), is(0L));

        final MemoryStatsConfig memoryStats = statistics.getMemoryStats();
        assertThat(memoryStats.getUsage(), is(647168L));
        assertThat(memoryStats.getMaxUsage(), is(1703936L));

        final StatsConfig stats = memoryStats.getStats();
        assertThat(stats.getActiveAnon(), is(102400L));
        assertThat(stats.getActiveFile(), is(0L));
        assertThat(stats.getCache(), is(0L));
        assertThat(stats.getDirty(), is(0L));
        assertThat(stats.getHierarchicalMemoryLimit(), is(9223372036854771712L));
        assertThat(stats.getHierarchicalMemswLimit(), is(9223372036854771712L));
        assertThat(stats.getInactiveAnon(), is(0L));
        assertThat(stats.getInactiveFile(), is(0L));
        assertThat(stats.getMappedFile(), is(0L));
        assertThat(stats.getPgfault(), is(9656L));
        assertThat(stats.getPgmajfault(), is(0L));
        assertThat(stats.getPgpgin(), is(3425L));
        assertThat(stats.getPgpgout(), is(3400L));
        assertThat(stats.getRss(), is(102400L));
        assertThat(stats.getRssHuge(), is(0L));
        assertThat(stats.getSwap(), is(0L));
        assertThat(stats.getTotalActiveAnon(), is(102400L));
        assertThat(stats.getTotalActiveFile(), is(0L));
        assertThat(stats.getTotalCache(), is(0L));
        assertThat(stats.getTotalDirty(), is(0L));
        assertThat(stats.getTotalInactiveAnon(), is(0L));
        assertThat(stats.getTotalInactiveFile(), is(0L));
        assertThat(stats.getTotalMappedFile(), is(0L));
        assertThat(stats.getTotalPgfault(), is(9656L));
        assertThat(stats.getTotalPgmajfault(), is(0L));
        assertThat(stats.getTotalPgpgin(), is(3425L));
        assertThat(stats.getTotalPgpgout(), is(3400L));
        assertThat(stats.getTotalRss(), is(102400L));
        assertThat(stats.getTotalRssHuge(), is(0L));
        assertThat(stats.getTotalSwap(), is(0L));
        assertThat(stats.getTotalUnevictable(), is(0L));
        assertThat(stats.getTotalWriteback(), is(0L));
        assertThat(stats.getUnevictable(), is(0L));
        assertThat(stats.getWriteback(), is(0L));

        assertThat(memoryStats.getLimit(), is(2095874048L));
        assertThat(memoryStats.getFailcnt(), is(0L));

        final BlkioStatsConfig blkioStats = statistics.getBlkioStats();
        assertThat(blkioStats.getIoServiceBytesRecursive(), Matchers.<BlkioStatEntry>hasSize(5));
        assertThat(blkioStats.getIoServiceBytesRecursive(), equalTo(Arrays.asList(
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Read").withValue(823296L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Write").withValue(122880L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Sync").withValue(835584L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Async").withValue(110592L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Total").withValue(946176L)
        )));

        assertThat(blkioStats.getIoServicedRecursive(), Matchers.<BlkioStatEntry>hasSize(5));
        assertThat(blkioStats.getIoServicedRecursive(), equalTo(Arrays.asList(
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Read").withValue(145L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Write").withValue(4L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Sync").withValue(148L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Async").withValue(1L),
            new BlkioStatEntry().withMajor(259L).withMinor(0L).withOp("Total").withValue(149L)
        )));
        assertThat(blkioStats.getIoQueueRecursive(), is(empty()));
        assertThat(blkioStats.getIoServiceTimeRecursive(), is(empty()));
        assertThat(blkioStats.getIoWaitTimeRecursive(), is(empty()));
        assertThat(blkioStats.getIoMergedRecursive(), is(empty()));
        assertThat(blkioStats.getIoTimeRecursive(), is(empty()));
        assertThat(blkioStats.getSectorsRecursive(), is(empty()));

        final CpuStatsConfig cpuStats = statistics.getCpuStats();
        final CpuUsageConfig cpuUsage = cpuStats.getCpuUsage();
        assertThat(cpuUsage.getTotalUsage(), is(212198028L));
        assertThat(cpuUsage.getPercpuUsage(), equalTo(Arrays.asList(71592953L, 42494761L, 59298344L, 38811970L)));
        assertThat(cpuUsage.getUsageInKernelmode(), is(170000000L));
        assertThat(cpuUsage.getUsageInUsermode(), is(20000000L));
        assertThat(cpuStats.getSystemCpuUsage(), is(545941980000000L));
        assertThat(cpuStats.getOnlineCpus(), is(4L));
        final ThrottlingDataConfig throttlingData = cpuStats.getThrottlingData();
        assertThat(throttlingData.getPeriods(), is(0L));
        assertThat(throttlingData.getThrottledPeriods(), is(0L));
        assertThat(throttlingData.getThrottledTime(), is(0L));

        final CpuStatsConfig preCpuStats = statistics.getPreCpuStats();
        final CpuUsageConfig preCpuUsage = preCpuStats.getCpuUsage();
        assertThat(preCpuUsage.getTotalUsage(), is(211307214L));
        assertThat(preCpuUsage.getPercpuUsage(), equalTo(Arrays.asList(71451389L, 42097782L, 59298344L, 38459699L)));
        assertThat(preCpuUsage.getUsageInKernelmode(), is(170000000L));
        assertThat(preCpuUsage.getUsageInUsermode(), is(20000000L));
        assertThat(preCpuStats.getSystemCpuUsage(), is(545937990000000L));
        assertThat(preCpuStats.getOnlineCpus(), is(4L));
        final ThrottlingDataConfig preThrottlingData = preCpuStats.getThrottlingData();
        assertThat(preThrottlingData.getPeriods(), is(0L));
        assertThat(preThrottlingData.getThrottledPeriods(), is(0L));
        assertThat(preThrottlingData.getThrottledTime(), is(0L));

        final PidsStatsConfig pidsStats = statistics.getPidsStats();
        assertThat(pidsStats.getCurrent(), is(2L));
    }
}
