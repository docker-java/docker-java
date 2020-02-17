package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.junit.DockerMatchers.apiVersionGreater;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

/**
 * @author Kanstantsin Shautsou
 */
public class AuthCmdIT extends CmdIT {

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testAuth() throws Exception {
        assumeThat("Fails on 1.22. Temporary disabled.", dockerRule, apiVersionGreater(VERSION_1_22));

        AuthResponse response = dockerRule.getClient().authCmd().exec();

        assertThat(response.getStatus(), is("Login Succeeded"));
    }



    @Ignore("Disabled because of 500/InternalServerException")
    @Test
    public void testAuthInvalid() throws Exception {
        expectedEx.expect(UnauthorizedException.class);
        expectedEx.expectMessage("Wrong login/password, please try again");

        DockerClientBuilder.getInstance(dockerRule.config("garbage"))
                .build()
                .authCmd()
                .exec();
    }
}
