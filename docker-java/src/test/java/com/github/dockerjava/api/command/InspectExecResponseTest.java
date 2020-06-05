package com.github.dockerjava.api.command;

import com.fasterxml.jackson.databind.JavaType;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Kanstantsin Shautsou
 */
public class InspectExecResponseTest {

    @Test
    public void test_1_22_SerDer1() throws Exception {
        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectExecResponse.class);

        final InspectExecResponse execResponse = testRoundTrip(RemoteApiVersion.VERSION_1_22,
                "/exec/ID/1.json",
                type
        );

        assertThat(execResponse, notNullValue());

        assertThat(execResponse.getId(),
                is("1ca2ca598fab202f86dd9281196c405456069013958a475396b707e85c56473b"));
        assertThat(execResponse.isRunning(), is(false));
        assertThat(execResponse.getExitCode(), is(nullValue()));

        final InspectExecResponse.ProcessConfig processConfig = execResponse.getProcessConfig();
        assertThat(processConfig, notNullValue());
        assertThat(processConfig.isTty(), is(false));
        assertThat(processConfig.getEntryPoint(), is("/bin/bash"));
        assertThat(processConfig.getArguments(), hasSize(0));
        assertThat(processConfig.isPrivileged(), is(false));
        assertThat(processConfig.getUser(), is(emptyString()));


        assertThat(execResponse.isOpenStdin(), is(false));
        assertThat(execResponse.isOpenStderr(), is(true));
        assertThat(execResponse.isOpenStdout(), is(true));
        assertThat(execResponse.getCanRemove(), is(false));
        assertThat(execResponse.getContainerID(),
                is("ffa39805f089af3099e36452a985481f96170a9dff40be69d34d1722c7660d38"));

        assertThat(execResponse.getDetachKeys(), is(emptyString()));
    }
}
