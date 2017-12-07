package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.RemoteApiVersion;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

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
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().uncheckedSimpleType(Statistics.class);

        final Statistics stat = testRoundTrip(RemoteApiVersion.VERSION_1_27,
                "containers/container/stats/stats1.json",
                type
        );

        assertThat(stat.getRead(), equalTo("2017-12-06T00:42:03.8352972Z"));

        final StatisticNetWorksConfig networkConfig = stat.getNetworks().get("eth0");
        assertThat(networkConfig.getRxBytes(), is(1230L));
        assertThat(networkConfig.getRxPackets(), is(19L));
        assertThat(networkConfig.getRxErrors(), is(0L));
        assertThat(networkConfig.getRxDropped(), is(0L));
        assertThat(networkConfig.getTxBytes(), is(0L));
        assertThat(networkConfig.getTxPackets(), is(0L));
        assertThat(networkConfig.getTxErrors(), is(0L));
        assertThat(networkConfig.getTxDropped(), is(0L));

        final MemoryStatsConfig memoryStatsConfig = stat.getMemoryStats();
        assertThat(memoryStatsConfig.getUsage(), is(647168L));
        assertThat(memoryStatsConfig.getMaxUsage(), is(1703936L));

        final StatsConfig statsConfig = memoryStatsConfig.getStats();
        assertThat(statsConfig.getActiveAnon(), is(102400L));
        assertThat(statsConfig.getActiveFile(), is(0L));
        assertThat(statsConfig.getCache(), is(0L));
        assertThat(statsConfig.getDirty(), is(0L));
        assertThat(statsConfig.getHierarchicalMemoryLimit(), is(9223372036854771712L));
        assertThat(statsConfig.getHierarchicalMemswLimit(), is(9223372036854771712L));
        assertThat(statsConfig.getInactiveAnon(), is(0L));
        assertThat(statsConfig.getInactiveFile(), is(0L));
        assertThat(statsConfig.getMappedFile(), is(0L));
        assertThat(statsConfig.getPgfault(), is(9656L));
        assertThat(statsConfig.getPgmajfault(), is(0L));
        assertThat(statsConfig.getPgpgin(), is(3425L));
        assertThat(statsConfig.getPgpgout(), is(3400L));
        assertThat(statsConfig.getRss(), is(102400L));
        assertThat(statsConfig.getRssHuge(), is(0L));
        assertThat(statsConfig.getSwap(), is(0L));
        assertThat(statsConfig.getTotalActiveAnon(), is(102400L));
        assertThat(statsConfig.getTotalActiveFile(), is(0L));
        assertThat(statsConfig.getTotalCache(), is(0L));
        assertThat(statsConfig.getTotalDirty(), is(0L));
        assertThat(statsConfig.getTotalInactiveAnon(), is(0L));
        assertThat(statsConfig.getTotalInactiveFile(), is(0L));
        assertThat(statsConfig.getTotalMappedFile(), is(0L));
        assertThat(statsConfig.getTotalPgfault(), is(9656L));
        assertThat(statsConfig.getTotalPgmajfault(), is(0L));
        assertThat(statsConfig.getTotalPgpgin(), is(3425L));
        assertThat(statsConfig.getTotalPgpgout(), is(3400L));
        assertThat(statsConfig.getTotalRss(), is(102400L));
        assertThat(statsConfig.getTotalRssHuge(), is(0L));
        assertThat(statsConfig.getTotalSwap(), is(0L));
        assertThat(statsConfig.getTotalUnevictable(), is(0L));
        assertThat(statsConfig.getTotalWriteback(), is(0L));
        assertThat(statsConfig.getUnevictable(), is(0L));
        assertThat(statsConfig.getWriteback(), is(0L));

        assertThat(memoryStatsConfig.getLimit(), is(2095874048L));

        final BlkioStatsConfig blkioStatsConfig = stat.getBlkioStats();
        assertThat(blkioStatsConfig.getIoServiceBytesRecursive(), is(empty()));
        assertThat(blkioStatsConfig.getIoServicedRecursive(), is(empty()));
        assertThat(blkioStatsConfig.getIoQueueRecursive(), is(empty()));
        assertThat(blkioStatsConfig.getIoServiceTimeRecursive(), is(empty()));
        assertThat(blkioStatsConfig.getIoWaitTimeRecursive(), is(empty()));
        assertThat(blkioStatsConfig.getIoMergedRecursive(), is(empty()));
        assertThat(blkioStatsConfig.getIoTimeRecursive(), is(empty()));
        assertThat(blkioStatsConfig.getSectorsRecursive(), is(empty()));


    }
}
