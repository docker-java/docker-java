package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.command.CreateSecretResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Secret;
import com.github.dockerjava.api.model.SecretSpec;
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

public class CreateSecretCmdExecIT extends SwarmCmdIT {

    public static final Logger LOG = LoggerFactory.getLogger(CreateSecretCmdExecIT.class);
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
        String secretName = RandomStringUtils.random(length, useLetters, useNumbers);
        CreateSecretResponse exec = dockerRule.getClient().createSecretCmd(new SecretSpec().withName(secretName).withData("mon secret en clair")).exec();
        assertThat(exec, notNullValue());
        assertThat(exec.getId(), notNullValue());
        LOG.info("Secret created with ID {}", exec.getId());


        List<Secret> secrets = dockerRule.getClient().listSecretsCmd()
                .withNameFilter(Lists.newArrayList(secretName))
                .exec();

        assertThat(secrets, IsCollectionWithSize.hasSize(1));

        dockerRule.getClient().removeSecretCmd(secrets.get(0).getId())
                .exec();
        LOG.info("Secret removed with ID {}", exec.getId());
        List<Secret> secretsAfterRemoved = dockerRule.getClient().listSecretsCmd()
                .withNameFilter(Lists.newArrayList(secretName))
                .exec();

        assertThat(secretsAfterRemoved, IsCollectionWithSize.hasSize(0));
    }

}
