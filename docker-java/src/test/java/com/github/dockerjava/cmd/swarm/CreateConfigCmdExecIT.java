package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateConfigResponse;
import com.github.dockerjava.api.model.Config;
import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        assertThat(response.getId(), notNullValue());
        LOG.info("Config created with ID {}", response.getId());

        List<Config> configs = dockerClient.listConfigsCmd()
                .withNameFilter(Lists.newArrayList(configName))
                .exec();

        assertThat(configs, IsCollectionWithSize.hasSize(1));

        dockerClient.removeConfigCmd(configs.get(0).getId())
                .exec();
        LOG.info("Config removed with ID {}", response.getId());
        List<Config> configsAfterRemoved = dockerClient.listConfigsCmd()
                .withNameFilter(Lists.newArrayList(configName))
                .exec();

        assertThat(configsAfterRemoved, IsCollectionWithSize.hasSize(0));
    }

}
