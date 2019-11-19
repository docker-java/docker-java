package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Base64;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.command.DockerCmd;
import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.command.SyncDockerCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.AuthConfig;

public abstract class AbstrDockerCmd<CMD_T extends DockerCmd<RES_T>, RES_T> implements SyncDockerCmd<RES_T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstrDockerCmd.class);

    protected DockerCmdSyncExec<CMD_T, RES_T> execution;

    protected final ObjectMapper objectMapper;

    @Deprecated
    public AbstrDockerCmd(DockerCmdSyncExec<CMD_T, RES_T> execution) {
        this(
                new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES),
                execution
        );
    }

    public AbstrDockerCmd(ObjectMapper objectMapper, DockerCmdSyncExec<CMD_T, RES_T> execution) {
        checkNotNull(objectMapper, "objectMapper was not specified");
        checkNotNull(execution, "execution was not specified");
        this.execution = execution;
        this.objectMapper = objectMapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RES_T exec() throws DockerException {
        LOGGER.debug("Cmd: {}", this);
        return execution.exec((CMD_T) this);
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SIMPLE_STYLE);
    }

    protected String registryAuth(AuthConfig authConfig) {
        try {
            return Base64.getEncoder().encodeToString(objectMapper.writeValueAsBytes(authConfig));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
