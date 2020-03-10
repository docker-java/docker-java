/*
 * Copyright 2015 CloudBees Inc., Oleg Nenashev.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.dockerjava.api.command;

import com.fasterxml.jackson.databind.JavaType;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.Isolation;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.test.serdes.JSONTestHelper;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static com.github.dockerjava.test.serdes.JSONTestHelper.testRoundTrip;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link InspectContainerResponse}.
 *
 * @author Oleg Nenashev
 */
public class InspectContainerResponseTest {

    @Test
    public void roundTrip_full() throws IOException {
        InspectContainerResponse[] responses = testRoundTrip(CommandJSONSamples.inspectContainerResponse_full,
            InspectContainerResponse[].class);
        assertEquals(1, responses.length);
        final InspectContainerResponse response = responses[0];

        // Check volumes: https://github.com/docker-java/docker-java/issues/211
        assertEquals(response.getVolumes().length, 2);
        assertEquals(response.getVolumesRW().length, 2);
        assertEquals(response.getVolumes()[1].getContainerPath(), "/bar/foo/myvol2");
        assertEquals(response.getVolumes()[1].getHostPath(), "/path2");
        assertEquals(response.getVolumesRW()[1].getVolume().getPath(), "/bar/foo/myvol2");
        assertFalse(response.getVolumesRW()[1].getAccessMode().toBoolean());
        assertTrue(response.getVolumesRW()[0].getAccessMode().toBoolean());
        assertThat(response.getLogPath(), is("/mnt/sda1/var/lib/docker/containers/469e5edd8d5b33e3c905a7ffc97360ec6ee211d6782815fbcd144568045819e1/469e5edd8d5b33e3c905a7ffc97360ec6ee211d6782815fbcd144568045819e1-json.log"));
    }

    @Test
    public void roundTrip_full_healthcheck() throws IOException {

        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectContainerResponse.class);

        final InspectContainerResponse response = testRoundTrip(RemoteApiVersion.VERSION_1_24,
            "/containers/inspect/1.json",
            type
        );

        assertEquals(response.getState().getHealth().getStatus(), "healthy");
        assertEquals(response.getState().getHealth().getFailingStreak(), new Integer(0));
        assertEquals(response.getState().getHealth().getLog().size(), 2);
        assertEquals(response.getState().getHealth().getLog().get(0).getOutput(), "Hello");
        assertEquals(response.getState().getHealth().getLog().get(1).getOutput(), "World");
    }

    @Test
    public void roundTrip_1_21_full() throws IOException {
        InspectContainerResponse[] responses = testRoundTrip(CommandJSONSamples.inspectContainerResponse_full_1_21,
            InspectContainerResponse[].class);
        assertEquals(1, responses.length);
        final InspectContainerResponse response = responses[0];
        final InspectContainerResponse.ContainerState state = response.getState();
        assertThat(state, not(nullValue()));

        assertFalse(state.getDead());
        assertThat(state.getStatus(), containsString("running"));
        assertFalse(state.getRestarting());
        assertFalse(state.getOOMKilled());
        assertThat(state.getError(), is(emptyString()));
    }

    @Test
    public void roundTrip_1_26a_full() throws IOException {
        InspectContainerResponse[] responses = testRoundTrip(CommandJSONSamples.inspectContainerResponse_full_1_26a,
            InspectContainerResponse[].class);

        assertEquals(1, responses.length);
        final InspectContainerResponse response = responses[0];

        final List<InspectContainerResponse.Mount> mounts = response.getMounts();
        assertEquals(mounts.size(), 1);

        final InspectContainerResponse.Mount mount = mounts.get(0);
        final Volume volume = mount.getDestination();
        assertEquals(volume.getPath(), "/var/lib/postgresql/data");
    }

    @Test
    public void roundTrip_1_26b_full() throws IOException {
        InspectContainerResponse[] responses = testRoundTrip(CommandJSONSamples.inspectContainerResponse_full_1_26b,
            InspectContainerResponse[].class);

        assertEquals(1, responses.length);
        final InspectContainerResponse response = responses[0];

        final List<InspectContainerResponse.Mount> mounts = response.getMounts();
        assertEquals(mounts.size(), 1);

        final InspectContainerResponse.Mount mount = mounts.get(0);
        final Volume volume = mount.getDestination();
        assertEquals(volume.getPath(), "/srv/test");
    }

    @Test
    public void roundTrip_empty() throws IOException {
        testRoundTrip(CommandJSONSamples.inspectContainerResponse_empty, InspectContainerResponse[].class);
    }

    @Test
    public void inspect_windows_container() throws IOException {

        final JavaType type = JSONTestHelper.getMapper().getTypeFactory().constructType(InspectContainerResponse.class);

        final InspectContainerResponse response = testRoundTrip(RemoteApiVersion.VERSION_1_38,
            "/containers/inspect/lcow.json",
            type
        );

        assertThat(response, notNullValue());

        assertThat(response.getConfig(), notNullValue());
        assertThat(response.getConfig().getCmd(), is(new String[]{"cmd"}));
        assertThat(response.getConfig().getImage(), is("microsoft/nanoserver"));

        assertThat(response.getDriver(), is("windowsfilter"));

        assertThat(response.getGraphDriver(), notNullValue());
        assertThat(response.getGraphDriver().getName(), is("windowsfilter"));
        assertThat(response.getGraphDriver().getData(), is(new GraphData().withDir(
            "C:\\ProgramData\\Docker\\windowsfilter\\35da02ca897bd378ee52be3066c847fee396ba1a28a00b4be36f42c6686bf556"
        )));

        assertThat(response.getHostConfig(), notNullValue());
        assertThat(response.getHostConfig().getIsolation(), is(Isolation.HYPERV));

        assertThat(response.getImageId(), is("sha256:1381511ec0122f197b6abff5bc0692bef19943ddafd6680eff41197afa3a6dda"));
        assertThat(response.getLogPath(), is(
            "C:\\ProgramData\\Docker\\containers\\35da02ca897bd378ee52be3066c847fee396ba1a28a00b4be36f42c6686bf556" +
                "\\35da02ca897bd378ee52be3066c847fee396ba1a28a00b4be36f42c6686bf556-json.log"
        ));
        assertThat(response.getName(), is("/cranky_clarke"));

        assertThat(response.getNetworkSettings(), notNullValue());
        assertThat(response.getNetworkSettings().getNetworks(), is(Collections.singletonMap("nat",
            new ContainerNetwork()
                .withEndpointId("493b77d6fe7e3b92435b1eb01461fde669781330deb84a9cbada360db8997ebc")
                .withGateway("172.17.18.1")
                .withGlobalIPv6Address("")
                .withGlobalIPv6PrefixLen(0)
                .withIpv4Address("172.17.18.123")
                .withIpPrefixLen(16)
                .withIpV6Gateway("")
                .withMacAddress("00:aa:ff:cf:dd:09")
                .withNetworkID("398c0e206dd677ed4a6566f9de458311f5767d8c7a8b963275490ab64c5d10a7")
        )));

        assertThat(response.getPath(), is("cmd"));
        assertThat(response.getPlatform(), is("windows"));
    }

    @Test
    public void equals() {
        assertThat(new InspectContainerResponse(), equalTo(new InspectContainerResponse()));
    }
}
