package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateSecretResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Secret;
import com.github.dockerjava.api.model.SecretSpec;
import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

public class ListSecretCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(ListSecretCmdExecIT.class);

    @Test
    public void tesListSecret() throws DockerException {
        DockerClient dockerClient = startSwarm();
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        String secretName = RandomStringUtils.random(length, useLetters, useNumbers);
        CreateSecretResponse exec = dockerClient.createSecretCmd(new SecretSpec().withName(secretName).withData("mon secret en clair")).exec();
        assertThat(exec, notNullValue());
        assertThat(exec.getId(), notNullValue());
        LOG.info("Secret created with ID {}", exec.getId());


        List<Secret> secrets = dockerClient.listSecretsCmd()
                .withNameFilter(Lists.newArrayList(secretName))
                .exec();

        assertThat(secrets, hasSize(1));

        dockerClient.removeSecretCmd(secrets.get(0).getId())
                .exec();
        LOG.info("Secret removed with ID {}", exec.getId());
        List<Secret> secretsAfterRemoved = dockerClient.listSecretsCmd()
                .withNameFilter(Lists.newArrayList(secretName))
                .exec();

        assertThat(secretsAfterRemoved, hasSize(0));

    }

}
