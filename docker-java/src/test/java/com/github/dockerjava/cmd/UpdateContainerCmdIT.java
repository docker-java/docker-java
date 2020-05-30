package com.github.dockerjava.cmd;

import com.fasterxml.jackson.databind.JavaType;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.command.UpdateContainerCmdImpl;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.junit.DockerMatchers.isGreaterOrEqual;
import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assume.assumeThat;

/**
 * @author Kanstantsin Shautsou
 */
public class UpdateContainerCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(UpdateContainerCmdIT.class);


    @Test
    public void updateContainer() throws DockerException, IOException {
        assumeThat("API version should be >= 1.22", dockerRule, isGreaterOrEqual(VERSION_1_22));

        CreateContainerResponse response = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("sleep", "9999")
                .exec();

        String containerId = response.getId();
        dockerRule.getClient().startContainerCmd(containerId).exec();

        InspectContainerResponse inspectBefore = dockerRule.getClient().inspectContainerCmd(containerId).exec();
        LOG.debug("Inspect: {}", inspectBefore);
        final Long memory = inspectBefore.getHostConfig().getMemory();

        dockerRule.getClient().updateContainerCmd(containerId)
                .withBlkioWeight(300)
                .withCpuShares(512)
                .withCpuPeriod(100000)
                .withCpuQuota(50000)
//                .withCpusetCpus("0") // depends on env
                .withCpusetMems("0")
//                .withMemory(209715200L + 2L)
//                .withMemorySwap(514288000L) Your kernel does not support swap limit capabilities, memory limited without swap.
//                .withMemoryReservation(209715200L)
//                .withKernelMemory(52428800) Can not update kernel memory to a running container, please stop it first.
                .exec();

        // true only on docker toolbox (1.10.1)
//        assertThat(updateResponse.getWarnings(), hasSize(1));
//        assertThat(updateResponse.getWarnings().get(0),
//                is("Your kernel does not support Block I/O weight. Weight discarded."));

        InspectContainerResponse inspectAfter = dockerRule.getClient().inspectContainerCmd(containerId).exec();
        final HostConfig afterHostConfig = inspectAfter.getHostConfig();

//        assertThat(afterHostConfig.getMemory(), is(209715200L + 2L));

//        assertThat(afterHostConfig.getBlkioWeight(), is(300));
        assertThat(afterHostConfig.getCpuShares(), is(512));
        assertThat(afterHostConfig.getCpuPeriod(), is(100000L));
        assertThat(afterHostConfig.getCpuQuota(), is(50000L));
        assertThat(afterHostConfig.getCpusetMems(), is("0"));

//        assertThat(afterHostConfig.getMemoryReservation(), is(209715200L));
//       assertThat(afterHostConfig.getMemorySwap(), is(514288000L));

    }

    @Ignore("impossible to serder because model bundled in cmd")
    @Test
    public void serDerDocs1() throws IOException {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(UpdateContainerCmdImpl.class);

        final UpdateContainerCmdImpl upd = testRoundTrip(VERSION_1_22,
                "/containers/container/update/docs.json",
                type
        );

        assertThat(upd, notNullValue());
    }
}
