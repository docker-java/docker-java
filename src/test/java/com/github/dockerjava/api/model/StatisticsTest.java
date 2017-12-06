package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.RemoteApiVersion;
import org.junit.Test;

import java.io.IOException;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.CoreMatchers.is;

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
    }
}
