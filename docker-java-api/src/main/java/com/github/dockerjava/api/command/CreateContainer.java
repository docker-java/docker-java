package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.NetworkingConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.Volumes;
import org.immutables.value.Value;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Value.Immutable
@ImmutableSpec
public interface CreateContainer {

    @Nullable
    String getName();

    @Nullable
    AuthConfig getAuthConfig();

    @JsonProperty("Image")
    String getImage();

    @Nullable
    @JsonProperty("HostName")
    String getHostName();

    @Nullable
    @JsonProperty("DomainName")
    String getDomainName();

    @Nullable
    @JsonProperty("User")
    String getUser();

    @Nullable
    @JsonProperty("AttachStdin")
    Boolean isAttachStdin();

    @Nullable
    @JsonProperty("AttachStdout")
    Boolean isAttachStdout();

    @Nullable
    @JsonProperty("AttachStderr")
    Boolean isAttachStderr();

    @Nullable
    @JsonProperty("PortSpecs")
    String[] getPortSpecs();

    @Nullable
    @JsonProperty("Tty")
    Boolean isTty();

    @Nullable
    @JsonProperty("OpenStdin")
    Boolean isStdinOpen();

    @Nullable
    @JsonProperty("StdInOnce")
    Boolean isStdInOnce();

    @Nullable
    @JsonProperty("Env")
    String[] getEnv();

    @Nullable
    @JsonProperty("Cmd")
    String[] getCmd();

    @Nullable
    @JsonProperty("Healthcheck")
    HealthCheck getHealthcheck();

    @Nullable
    @JsonProperty("ArgsEscaped")
    Boolean getArgsEscaped();

    @Nullable
    @JsonProperty("Entrypoint")
    String[] getEntrypoint();

    @Nullable
    Volume[] getVolumes();

    /**
     *
     * @deprecated do not use/implement directly, see {@link #getVolumes()} instead
     */
    @Nullable
    @JsonProperty("Volumes")
    @Deprecated
    @Value.Auxiliary
    @SuppressWarnings("checkstyle:methodname")
    default Volumes $$_JsonFriendlyVolumes() {
        Volume[] volumes = getVolumes();
        return volumes == null
                ? null
                : new Volumes(volumes);
    }

    @Nullable
    @JsonProperty("WorkingDir")
    String getWorkingDir();

    @Nullable
    @JsonProperty("MacAddress")
    String getMacAddress();

    @Nullable
    @JsonProperty("OnBuild")
    List<String> getOnBuild();

    @Nullable
    @JsonProperty("NetworkDisabled")
    Boolean isNetworkDisabled();

    @Nullable
    ExposedPort[] getExposedPorts();

    /**
     *
     * @deprecated do not use/implement directly, see {@link #getExposedPorts()} instead
     */
    @Nullable
    @JsonProperty("ExposedPorts")
    @Deprecated
    @Value.Auxiliary
    @SuppressWarnings("checkstyle:methodname")
    default ExposedPorts $$_JsonFriendlyExposedPorts() {
        ExposedPort[] exposedPorts = getExposedPorts();
        return exposedPorts == null
                ? null
                : new ExposedPorts(exposedPorts);
    }

    @Nullable
    @JsonProperty("StopSignal")
    String getStopSignal();

    @Nullable
    @JsonProperty("StopTimeout")
    Integer getStopTimeout();

    @JsonProperty("HostConfig")
    default HostConfig getHostConfig() {
        return new HostConfig();
    }

    @Nullable
    @JsonProperty("Labels")
    Map<String, String> getLabels();

    @Nullable
    @JsonProperty("NetworkingConfig")
    NetworkingConfig getNetworkingConfig();

    @CheckForNull
    @JsonProperty("Ipv4Address")
    String getIpv4Address();

    @CheckForNull
    @JsonProperty("Ipv6Address")
    String getIpv6Address();
}
