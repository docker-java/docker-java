package com.github.dockerjava.cmd;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.github.dockerjava.api.model.AuthResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import org.junit.Ignore;
import org.junit.Test;

import static com.github.dockerjava.core.RemoteApiVersion.VERSION_1_22;
import static com.github.dockerjava.junit.DockerMatchers.apiVersionGreater;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.Assume.assumeThat;

/**
 * @author Kanstantsin Shautsou
 */
public class AuthCmdIT extends CmdIT {

    @Test
    public void testAuth() throws Exception {
        assumeThat("Fails on 1.22. Temporary disabled.", dockerRule, apiVersionGreater(VERSION_1_22));

        AuthResponse response = dockerRule.getClient().authCmd().exec();

        assertThat(response.getStatus(), is("Login Succeeded"));
    }


    @Ignore("Disabled because of 500/InternalServerException")
    @Test
    public void testAuthInvalid() throws Exception {
        assertThrows("Wrong login/password, please try again", UnauthorizedException.class, () -> {
            DockerClientBuilder.getInstance(dockerRule.config("garbage"))
                .build()
                .authCmd()
                .exec();
        });
    }
}
