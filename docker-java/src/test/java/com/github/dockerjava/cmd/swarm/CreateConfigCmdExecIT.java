package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.command.CreateConfigResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Config;
import com.github.dockerjava.api.model.ConfigSpec;
import com.github.dockerjava.api.model.Secret;
import com.github.dockerjava.api.model.SwarmSpec;
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
    private static final String SERVICE_NAME = "theservice";

    @Test
    public void testCreateSecret() throws DockerException {
        dockerRule.getClient().initializeSwarmCmd(new SwarmSpec())
                .withListenAddr("127.0.0.1")
                .withAdvertiseAddr("127.0.0.1")
                .exec();

        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String configName = RandomStringUtils.random(length, useLetters, useNumbers);
        CreateConfigResponse exec = dockerRule.getClient().createConfigCmd(new ConfigSpec().withName(configName).withData("mon secret en clair".getBytes())).exec();
        assertThat(exec, notNullValue());
        assertThat(exec.getId(), notNullValue());
        LOG.info("Config created with ID {}", exec.getId());


        List<Config> configs = dockerRule.getClient().listConfigsCmd()
                .withNameFilter(Lists.newArrayList(configName))
                .exec();

        assertThat(configs, IsCollectionWithSize.hasSize(1));

        dockerRule.getClient().removeConfigCmd(configs.get(0).getId())
                .exec();
        LOG.info("Config removed with ID {}", exec.getId());
        List<Secret> configsAfterRemoved = dockerRule.getClient().listSecretsCmd()
                .withNameFilter(Lists.newArrayList(configName))
                .exec();

        assertThat(configsAfterRemoved, IsCollectionWithSize.hasSize(0));
    }

}
