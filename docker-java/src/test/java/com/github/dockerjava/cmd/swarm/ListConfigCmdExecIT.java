package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateConfigResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Config;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ListConfigCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(ListConfigCmdExecIT.class);

    @Test
    public void tesListConfig() throws DockerException {
        DockerClient dockerClient = startSwarm();
        String configName = RandomStringUtils.random(10, true, false);
        CreateConfigResponse response = dockerClient.createConfigCmd()
            .withName(configName)
            .withData("configuration data".getBytes())
            .exec();
        String configId = response.getId();

        try {
            LOG.info("Config created with ID {}", configId);

            List<Config> configs = dockerClient.listConfigsCmd()
                .withFilters(Collections.singletonMap("name", Arrays.asList(configName)))
                .exec();

            assertThat(configs, hasSize(1));
        } finally {
            dockerClient.removeConfigCmd(configId).exec();
            LOG.info("Config removed with ID {}", configId);
        }

        List<Config> configsAfterRemoved = dockerClient.listConfigsCmd()
            .withFilters(Collections.singletonMap("name", Arrays.asList(configName)))
            .exec();

        assertThat(configsAfterRemoved, hasSize(0));

    }

}
