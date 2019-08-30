package com.github.dockerjava.cmd.swarm;

import com.github.dockerjava.api.command.CreateSecretResponse;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.ContainerSpec;
import com.github.dockerjava.api.model.EndpointResolutionMode;
import com.github.dockerjava.api.model.EndpointSpec;
import com.github.dockerjava.api.model.Mount;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.NetworkAttachmentConfig;
import com.github.dockerjava.api.model.PortConfig;
import com.github.dockerjava.api.model.PortConfigProtocol;
import com.github.dockerjava.api.model.Secret;
import com.github.dockerjava.api.model.SecretSpec;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.api.model.ServiceModeConfig;
import com.github.dockerjava.api.model.ServiceReplicatedModeOptions;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.api.model.SwarmSpec;
import com.github.dockerjava.api.model.TaskSpec;
import com.github.dockerjava.api.model.TmpfsOptions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
