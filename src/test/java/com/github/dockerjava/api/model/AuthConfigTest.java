package com.github.dockerjava.api.model;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.core.RemoteApiVersion;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.github.dockerjava.test.serdes.JSONSamples.testRoundTrip;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertEquals;

public class AuthConfigTest {

    @Test
    public void defaultServerAddress() throws Exception {
        assertEquals(new AuthConfig().getRegistryAddress(), "https://index.docker.io/v1/");
    }

    @Test
    public void serderDocs1() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().uncheckedSimpleType(AuthConfig.class);

        final AuthConfig authConfig = testRoundTrip(RemoteApiVersion.VERSION_1_22,
                "/other/AuthConfig/docs1.json",
                type
        );

        assertThat(authConfig, notNullValue());
        assertThat(authConfig.getUsername(), is("jdoe"));
        assertThat(authConfig.getPassword(), is("secret"));
        assertThat(authConfig.getEmail(), is("jdoe@acme.com"));

        final AuthConfig authConfig1 = new AuthConfig().withUsername("jdoe")
                .withPassword("secret")
                .withEmail("jdoe@acme.com");

        assertThat(authConfig1, equalTo(authConfig));
    }

    @Test
    public void serderDocs2() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JavaType type = mapper.getTypeFactory().uncheckedSimpleType(AuthConfig.class);

        final AuthConfig authConfig = testRoundTrip(RemoteApiVersion.VERSION_1_22,
                "/other/AuthConfig/docs2.json",
                type
        );

        assertThat(authConfig, notNullValue());
        assertThat(authConfig.getRegistrytoken(), is("9cbaf023786cd7..."));


        final AuthConfig authConfig1 = new AuthConfig().withRegistrytoken("9cbaf023786cd7...");

        assertThat(authConfig1, equalTo(authConfig));
    }
}
