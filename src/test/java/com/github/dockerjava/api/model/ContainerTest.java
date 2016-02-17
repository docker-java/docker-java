package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * @author Kanstantsin Shautsou
 */
public class ContainerTest {

    @Test
    public void serderJson1() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, Container.class);

        final List<Container> containers = testRoundTrip(RemoteApiVersion.VERSION_1_22,
                "containers/json/filter1.json",
                type
        );

        assertThat(containers.size(), equalTo(1));

        final Container container = containers.get(0);

        assertThat(container.getImageId(),
                equalTo("sha256:0cb40641836c461bc97c793971d84d758371ed682042457523e4ae701efe7ec9"));
        assertThat(container.getSizeRootFs(), equalTo(1113554L));

        final ContainerHostConfig hostConfig = container.getHostConfig();
        assertThat(hostConfig, notNullValue());
        assertThat(hostConfig.getNetworkMode(), equalTo("default"));
    }

}
