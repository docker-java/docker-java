package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateConfigResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Config;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class InspectConfigCmdIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(InspectConfigCmdIT.class);

    @Test
    public void inspectConfig() throws DockerException {
        DockerClient dockerClient = startSwarm();

        String configName = RandomStringUtils.random(10, true, false);

        CreateConfigResponse configResponse = dockerClient.createConfigCmd()
            .withName(configName)
            .withData("configuration data".getBytes()).exec();
        LOG.info("Config created with ID {}", configResponse.getId());

        Config config = dockerClient.inspectConfigCmd(configResponse.getId()).exec();
        assertEquals(configResponse.getId(), config.getId());
        assertEquals(configName, config.getSpec().getName());
    }
}
