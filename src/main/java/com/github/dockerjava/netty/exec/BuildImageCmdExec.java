package com.github.dockerjava.netty.exec;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.netty.InvocationBuilder;
import com.github.dockerjava.netty.MediaType;
import com.github.dockerjava.netty.WebTarget;

import java.io.IOException;

public class BuildImageCmdExec extends AbstrAsyncDockerCmdExec<BuildImageCmd, BuildResponseItem> implements
        BuildImageCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildImageCmdExec.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public BuildImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    private InvocationBuilder resourceWithOptionalAuthConfig(BuildImageCmd command, InvocationBuilder request) {
        final AuthConfigurations authConfigs = firstNonNull(command.getBuildAuthConfigs(), getBuildAuthConfigs());
        if (authConfigs != null && !authConfigs.getConfigs().isEmpty()) {
            request = request.header("X-Registry-Config", registryConfigs(authConfigs));
        }
        return request;
    }

    private static AuthConfigurations firstNonNull(final AuthConfigurations fromCommand,
            final AuthConfigurations fromConfig) {
        if (fromCommand != null) {
            return fromCommand;
        }
        if (fromConfig != null) {
            return fromConfig;
        }
        return null;
    }

    @Override
    protected Void execute0(BuildImageCmd command, ResultCallback<BuildResponseItem> resultCallback) {

        WebTarget webTarget = getBaseResource().path("/build");
        String dockerFilePath = command.getPathToDockerfile();

        if (dockerFilePath != null && command.getRemote() == null && !"Dockerfile".equals(dockerFilePath)) {
            webTarget = webTarget.queryParam("dockerfile", dockerFilePath);
        }
        if (command.getTag() != null) {
            webTarget = webTarget.queryParam("t", command.getTag());
        }
        if (command.getRemote() != null) {
            webTarget = webTarget.queryParam("remote", command.getRemote().toString());
        }

        webTarget = booleanQueryParam(webTarget, "q", command.isQuiet());
        webTarget = booleanQueryParam(webTarget, "nocache", command.hasNoCacheEnabled());
        webTarget = booleanQueryParam(webTarget, "pull", command.hasPullEnabled());
        webTarget = booleanQueryParam(webTarget, "rm", command.hasRemoveEnabled());
        webTarget = booleanQueryParam(webTarget, "forcerm", command.isForcerm());

        // this has to be handled differently as it should switch to 'false'
        if (command.hasRemoveEnabled() == null || !command.hasRemoveEnabled()) {
            webTarget = webTarget.queryParam("rm", "false");
        }

        if (command.getMemory() != null) {
            webTarget = webTarget.queryParam("memory", command.getMemory());
        }
        if (command.getMemswap() != null) {
            webTarget = webTarget.queryParam("memswap", command.getMemswap());
        }
        if (command.getCpushares() != null) {
            webTarget = webTarget.queryParam("cpushares", command.getCpushares());
        }
        if (command.getCpusetcpus() != null) {
            webTarget = webTarget.queryParam("cpusetcpus", command.getCpusetcpus());
        }

        if (command.getBuildArgs() != null && !command.getBuildArgs().isEmpty()) {
            try {
                webTarget = webTarget.queryParam("buildargs", MAPPER.writeValueAsString(command.getBuildArgs()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (command.getShmsize() != 0) {
            webTarget = webTarget.queryParam("shmsize", command.getShmsize());
        }

        LOGGER.trace("POST: {}", webTarget);

        InvocationBuilder builder = resourceWithOptionalAuthConfig(command, webTarget.request())
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/tar")
                .header("encoding", "gzip");

        builder.post(new TypeReference<BuildResponseItem>() {
        }, resultCallback, command.getTarInputStream());

        return null;
    }
}
