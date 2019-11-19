package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;
import static org.apache.commons.lang.StringUtils.isNotBlank;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.github.dockerjava.core.util.CacheFromEncoder;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.async.JsonStreamProcessor;
import com.github.dockerjava.jaxrs.async.AbstractCallbackNotifier;
import com.github.dockerjava.jaxrs.async.POSTCallbackNotifier;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

public class BuildImageCmdExec extends AbstrAsyncDockerCmdExec<BuildImageCmd, BuildResponseItem> implements
        BuildImageCmd.Exec {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildImageCmdExec.class);

    public BuildImageCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    private Invocation.Builder resourceWithOptionalAuthConfig(BuildImageCmd command, Invocation.Builder request) {
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
    protected AbstractCallbackNotifier<BuildResponseItem> callbackNotifier(BuildImageCmd command,
            ResultCallback<BuildResponseItem> resultCallback) {

        WebTarget webTarget = getBaseResource().path("/build");
        String dockerFilePath = command.getPathToDockerfile();

        if (dockerFilePath != null && command.getRemote() == null && !"Dockerfile".equals(dockerFilePath)) {
            webTarget = webTarget.queryParam("dockerfile", dockerFilePath);
        }

        if (command.getTags() != null && !command.getTags().isEmpty()) {
            for (String t : command.getTags()) {
                webTarget = webTarget.queryParam("t", t);
            }
        } else if (isNotBlank(command.getTag())) {
            webTarget = webTarget.queryParam("t", command.getTag());
        }

        if (command.getCacheFrom() != null && !command.getCacheFrom().isEmpty()) {
            webTarget = webTarget.queryParam("cachefrom", CacheFromEncoder.jsonEncode(command.getCacheFrom()));
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

        if (command.hasRemoveEnabled() == null || !command.hasRemoveEnabled()) {
            webTarget = webTarget.queryParam("rm", "false");
        }

        webTarget = writeMap(webTarget, "buildargs", command.getBuildArgs());

        if (command.getShmsize() != null) {
            webTarget = webTarget.queryParam("shmsize", command.getShmsize());
        }

        webTarget = writeMap(webTarget, "labels", command.getLabels());

        if (command.getNetworkMode() != null) {
            webTarget = webTarget.queryParam("networkmode", command.getNetworkMode());
        }

        if (command.getPlatform() != null) {
            webTarget = webTarget.queryParam("platform", command.getPlatform());
        }

        if (command.getTarget() != null) {
            webTarget = webTarget.queryParam("target", command.getTarget());
        }

        webTarget.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.CHUNKED);
        webTarget.property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024 * 1024);

        LOGGER.trace("POST: {}", webTarget);

        return new POSTCallbackNotifier<>(new JsonStreamProcessor<>(objectMapper, BuildResponseItem.class),
                resultCallback,
                resourceWithOptionalAuthConfig(command, webTarget.request()).accept(MediaType.TEXT_PLAIN),
                entity(command.getTarInputStream(), "application/tar")
        );
    }

    private WebTarget writeMap(WebTarget webTarget, String name, Map<String, String> value) {
        if (value != null && !value.isEmpty()) {
            try {
                return webTarget.queryParam(name,
                        URLEncoder.encode(objectMapper.writeValueAsString(value), "UTF-8").replaceAll("\\+", "%20"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return webTarget;
        }
    }
}
