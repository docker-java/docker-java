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

import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.dockerjava.test.serdes.JSONTestHelper.testRoundTrip;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNot.not;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
        assertThat(state.getError(), isEmptyString());
    }

    @Test
    public void roundTrip_empty() throws IOException {
        testRoundTrip(CommandJSONSamples.inspectContainerResponse_empty, InspectContainerResponse[].class);
    }
}
