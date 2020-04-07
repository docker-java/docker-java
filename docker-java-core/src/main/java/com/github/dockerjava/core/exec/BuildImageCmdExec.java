package com.github.dockerjava.core.exec;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.InvocationBuilder;
import com.github.dockerjava.core.MediaType;
import com.github.dockerjava.core.WebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;

import static com.github.dockerjava.core.util.CacheFromEncoder.jsonEncode;
import static org.apache.commons.lang.StringUtils.isNotBlank;

public class BuildImageCmdExec extends AbstrAsyncDockerCmdExec<BuildImageCmd, BuildResponseItem> implements
        BuildImageCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildImageCmdExec.class);

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

    @CheckForNull
    private static AuthConfigurations firstNonNull(@CheckForNull final AuthConfigurations fromCommand,
            @CheckForNull final AuthConfigurations fromConfig) {
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

        if (command.getTags() != null && !command.getTags().isEmpty()) {
            webTarget = webTarget.queryParamsSet("t", command.getTags());
        } else if (isNotBlank(command.getTag())) {
            webTarget = webTarget.queryParam("t", command.getTag());
        }

        if (command.getCacheFrom() != null && !command.getCacheFrom().isEmpty()) {
            webTarget = webTarget.queryParam("cachefrom", jsonEncode(command.getCacheFrom()));
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

        if (command.getBuildArgs() != null) {
            webTarget = webTarget.queryParamsJsonMap("buildargs", command.getBuildArgs());
        }

        if (command.getShmsize() != null) {
            webTarget = webTarget.queryParam("shmsize", command.getShmsize());
        }

        if (command.getLabels() != null) {
            webTarget = webTarget.queryParamsJsonMap("labels", command.getLabels());
        }

        if (command.getNetworkMode() != null) {
            webTarget = webTarget.queryParam("networkmode", command.getNetworkMode());
        }

        if (command.getPlatform() != null) {
            webTarget = webTarget.queryParam("platform", command.getPlatform());
        }

        if (command.getTarget() != null) {
            webTarget = webTarget.queryParam("target", command.getTarget());
        }

        if (command.getExtraHosts() != null) {
            webTarget = webTarget.queryParamsSet("extrahosts", command.getExtraHosts());
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
