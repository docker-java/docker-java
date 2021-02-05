package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateConfigResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class CreateConfigCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(CreateConfigCmdExecIT.class);

    @Test
    public void testCreateConfig() {
        DockerClient dockerClient = startSwarm();
        String configName = RandomStringUtils.random(10, true, false);
        CreateConfigResponse response = dockerClient.createConfigCmd()
            .withName(configName)
            .withData("configuration data".getBytes()).exec();
        assertThat(response, notNullValue());
        String configId = response.getId();
        assertThat(configId, notNullValue());

        dockerClient.removeConfigCmd(configId).exec();
    }

}
