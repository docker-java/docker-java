package com.github.dockerjava.core.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.CreateContainerSpec;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

/**
 * Creates a new container.
 * `/containers/create`
 */
public class CreateContainerCmdImpl extends AbstrDockerCmd<CreateContainerCmd, CreateContainerResponse> implements
        CreateContainerCmd {

    private CreateContainerSpec spec;

    private String ipv4Address = null;

    private String ipv6Address = null;

    private List<String> aliases = null;

    public CreateContainerCmdImpl(CreateContainerCmd.Exec exec, AuthConfig authConfig, String image) {
        this(
            exec,
            CreateContainerSpec.builder()
                .image(image)
                .authConfig(authConfig)
                .build()
        );
    }

    CreateContainerCmdImpl(CreateContainerCmd.Exec createContainerCmdExec, CreateContainerSpec spec) {
        super(createContainerCmdExec);
        this.spec = CreateContainerSpec.copyOf(spec);
    }

    public AuthConfig getAuthConfig() {
        return spec.getAuthConfig();
    }

    public CreateContainerCmd withAuthConfig(AuthConfig authConfig) {
        this.spec = spec.withAuthConfig(authConfig);
        return this;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public CreateContainerCmd withAliases(String... aliases) {
        this.aliases = Arrays.asList(aliases);
        return this;
    }

    @Override
    public CreateContainerCmd withAliases(List<String> aliases) {
        requireNonNull(aliases, "aliases was not specified");
        this.aliases = aliases;
        return this;
    }


    @Override
    public String[] getCmd() {
        return spec.getCmd();
    }

    @Override
    public CreateContainerCmd withCmd(String... cmd) {
        requireNonNull(cmd, "cmd was not specified");
        this.spec = spec.withCmd(cmd);
        return this;
    }

    @Override
    public CreateContainerCmd withCmd(List<String> cmd) {
        requireNonNull(cmd, "cmd was not specified");
        return withCmd(cmd.toArray(new String[0]));
    }

    @CheckForNull
    public HealthCheck getHealthcheck() {
        return spec.getHealthcheck();
    }

    public CreateContainerCmdImpl withHealthcheck(HealthCheck healthcheck) {
        this.spec = spec.withHealthcheck(healthcheck);
        return this;
    }

    public Boolean getArgsEscaped() {
        return spec.getArgsEscaped();
    }

    public CreateContainerCmdImpl withArgsEscaped(Boolean argsEscaped) {
        this.spec = spec.withArgsEscaped(argsEscaped);
        return this;
    }

    @Override
    public String getDomainName() {
        return spec.getDomainName();
    }

    @Override
    public CreateContainerCmd withDomainName(String domainName) {
        requireNonNull(domainName, "no domainName was specified");
        this.spec = spec.withDomainName(domainName);
        return this;
    }

    @Override
    public String[] getEntrypoint() {
        return spec.getEntrypoint();
    }

    @Override
    public CreateContainerCmd withEntrypoint(String... entrypoint) {
        requireNonNull(entrypoint, "entrypoint was not specified");
        this.spec = spec.withEntrypoint(entrypoint);
        return this;
    }

    @Override
    public CreateContainerCmd withEntrypoint(List<String> entrypoint) {
        requireNonNull(entrypoint, "entrypoint was not specified");
        return withEntrypoint(entrypoint.toArray(new String[0]));
    }

    @Override
    public String[] getEnv() {
        return spec.getEnv();
    }

    @Override
    public CreateContainerCmd withEnv(String... env) {
        requireNonNull(env, "env was not specified");
        this.spec = spec.withEnv(env);
        return this;
    }

    @Override
    public CreateContainerCmd withEnv(List<String> env) {
        requireNonNull(env, "env was not specified");
        return withEnv(env.toArray(new String[0]));
    }

    @Override
    public ExposedPort[] getExposedPorts() {
        return spec.getExposedPorts();
    }

    @Override
    public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts) {
        requireNonNull(exposedPorts, "exposedPorts was not specified");
        this.spec = spec.withExposedPorts(exposedPorts);
        return this;
    }

    @Override
    public CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts) {
        requireNonNull(exposedPorts, "exposedPorts was not specified");
        return withExposedPorts(exposedPorts.toArray(new ExposedPort[0]));
    }

    /**
     * @see #stopSignal
     */
    @Override
    public String getStopSignal() {
        return spec.getStopSignal();
    }

    @Override
    public CreateContainerCmd withStopSignal(String stopSignal) {
        requireNonNull(stopSignal, "stopSignal wasn't specified.");
        this.spec = spec.withStopSignal(stopSignal);
        return this;
    }

    @Override
    public Integer getStopTimeout() {
        return spec.getStopTimeout();
    }

    @Override
    public CreateContainerCmd withStopTimeout(Integer stopTimeout) {
        this.spec = spec.withStopTimeout(stopTimeout);
        return this;
    }

    @Override
    public String getHostName() {
        return spec.getHostName();
    }

    @Override
    public CreateContainerCmd withHostName(String hostName) {
        requireNonNull(hostName, "no hostName was specified");
        this.spec = spec.withHostName(hostName);
        return this;
    }

    @Override
    public String getImage() {
        return spec.getImage();
    }

    @Override
    public CreateContainerCmd withImage(String image) {
        requireNonNull(image, "no image was specified");
        this.spec = spec.withImage(image);
        return this;
    }

    @Override
    public Map<String, String> getLabels() {
        return spec.getLabels();
    }

    @Override
    public CreateContainerCmd withLabels(Map<String, String> labels) {
        requireNonNull(labels, "labels was not specified");
        labels = new HashMap<>(labels);
        labels.replaceAll((key, value) -> value == null ? "" : value);
        this.spec = spec.withLabels(labels);
        return this;
    }

    @Override
    public String getMacAddress() {
        return spec.getMacAddress();
    }

    @Override
    public CreateContainerCmd withMacAddress(String macAddress) {
        requireNonNull(macAddress, "macAddress was not specified");
        this.spec = spec.withMacAddress(macAddress);
        return this;
    }

    @Override
    public String getName() {
        return spec.getName();
    }

    @Override
    public CreateContainerCmd withName(String name) {
        requireNonNull(name, "name was not specified");
        this.spec = spec.withName(name);
        return this;
    }

    @Override
    public String[] getPortSpecs() {
        return spec.getPortSpecs();
    }

    @Override
    public CreateContainerCmd withPortSpecs(String... portSpecs) {
        requireNonNull(portSpecs, "portSpecs was not specified");
        this.spec = spec.withPortSpecs(portSpecs);
        return this;
    }

    @Override
    public CreateContainerCmd withPortSpecs(List<String> portSpecs) {
        requireNonNull(portSpecs, "portSpecs was not specified");
        return withPortSpecs(portSpecs.toArray(new String[0]));
    }

    @Override
    public String getUser() {
        return spec.getUser();
    }

    @Override
    public CreateContainerCmd withUser(String user) {
        requireNonNull(user, "user was not specified");
        this.spec = spec.withUser(user);
        return this;
    }

    @Override
    public Boolean isAttachStderr() {
        return spec.isAttachStderr();
    }

    @Override
    public CreateContainerCmd withAttachStderr(Boolean attachStderr) {
        requireNonNull(attachStderr, "attachStderr was not specified");
        this.spec = spec.withAttachStderr(attachStderr);
        return this;
    }

    @Override
    public Boolean isAttachStdin() {
        return spec.isAttachStdin();
    }

    @Override
    public CreateContainerCmd withAttachStdin(Boolean attachStdin) {
        requireNonNull(attachStdin, "attachStdin was not specified");
        this.spec = spec.withAttachStdin(attachStdin);
        return this;
    }

    @Override
    public Boolean isAttachStdout() {
        return spec.isAttachStdout();
    }

    @Override
    public CreateContainerCmd withAttachStdout(Boolean attachStdout) {
        requireNonNull(attachStdout, "attachStdout was not specified");
        this.spec = spec.withAttachStdout(attachStdout);
        return this;
    }

    @Override
    public Volume[] getVolumes() {
        return spec.getVolumes();
    }

    @Override
    public CreateContainerCmd withVolumes(Volume... volumes) {
        requireNonNull(volumes, "volumes was not specified");
        this.spec = spec.withVolumes(volumes);
        return this;
    }

    @Override
    public CreateContainerCmd withVolumes(List<Volume> volumes) {
        requireNonNull(volumes, "volumes was not specified");
        return withVolumes(volumes.toArray(new Volume[0]));
    }

    @Override
    public String getWorkingDir() {
        return spec.getWorkingDir();
    }

    @Override
    public CreateContainerCmd withWorkingDir(String workingDir) {
        requireNonNull(workingDir, "workingDir was not specified");
        this.spec = spec.withWorkingDir(workingDir);
        return this;
    }

    @Override
    public Boolean isNetworkDisabled() {
        return spec.isNetworkDisabled();
    }

    @Override
    public CreateContainerCmd withNetworkDisabled(Boolean disableNetwork) {
        requireNonNull(disableNetwork, "disableNetwork was not specified");
        this.spec = spec.withNetworkDisabled(disableNetwork);
        return this;
    }


    @Override
    public Boolean isStdInOnce() {
        return spec.isStdInOnce();
    }

    @Override
    public CreateContainerCmd withStdInOnce(Boolean stdInOnce) {
        requireNonNull(stdInOnce, "no stdInOnce was specified");
        this.spec = spec.withStdInOnce(stdInOnce);
        return this;
    }

    @Override
    public Boolean isStdinOpen() {
        return spec.isStdinOpen();
    }

    @Override
    public CreateContainerCmd withStdinOpen(Boolean stdinOpen) {
        requireNonNull(stdinOpen, "no stdinOpen was specified");
        this.spec = spec.withStdinOpen(stdinOpen);
        return this;
    }


    @Override
    public Boolean isTty() {
        return spec.isTty();
    }

    @Override
    public CreateContainerCmd withTty(Boolean tty) {
        requireNonNull(tty, "no tty was specified");
        this.spec = spec.withTty(tty);
        return this;
    }

    @Override
    public HostConfig getHostConfig() {
        return spec.getHostConfig();
    }

    @Override
    public CreateContainerCmd withHostConfig(HostConfig hostConfig) {
        this.spec = spec.withHostConfig(hostConfig);
        return this;
    }

    @Override
    public String getIpv4Address() {
        return ipv4Address;
    }

    @Override
    public CreateContainerCmd withIpv4Address(String ipv4Address) {
        requireNonNull(ipv4Address, "no ipv4Address was specified");
        this.ipv4Address = ipv4Address;
        return this;
    }

    @Override
    public String getIpv6Address() {
        return ipv6Address;
    }

    @Override
    public CreateContainerCmd withIpv6Address(String ipv6Address) {
        requireNonNull(ipv6Address, "no ipv6Address was specified");
        this.ipv6Address = ipv6Address;
        return this;
    }

    @CheckForNull
    public List<String> getOnBuild() {
        return spec.getOnBuild();
    }

    public CreateContainerCmdImpl withOnBuild(List<String> onBuild) {
        this.spec = spec.withOnBuild(onBuild);
        return this;
    }

    /**
     * @throws NotFoundException No such container
     * @throws ConflictException Named container already exists
     */
    @Override
    public CreateContainerResponse exec() throws NotFoundException, ConflictException {
        //code flow taken from https://github.com/docker/docker/blob/master/runconfig/opts/parse.go
        ContainerNetwork containerNetwork = null;

        if (ipv4Address != null || ipv6Address != null) {
            containerNetwork = new ContainerNetwork()
                    .withIpamConfig(new ContainerNetwork.Ipam()
                            .withIpv4Address(ipv4Address)
                            .withIpv6Address(ipv6Address)
                    );

        }
        HostConfig hostConfig = getHostConfig();

        if (hostConfig.isUserDefinedNetwork() && hostConfig.getLinks().length > 0) {
            if (containerNetwork == null) {
                containerNetwork = new ContainerNetwork();
            }

            containerNetwork.withLinks(hostConfig.getLinks());
        }

        if (aliases != null) {
            if (containerNetwork == null) {
                containerNetwork = new ContainerNetwork();
            }

            containerNetwork.withAliases(aliases);
        }

        if (containerNetwork != null) {
            spec = spec.withNetworkingConfig(
                    new com.github.dockerjava.api.model.NetworkingConfig()
                            .withEndpointsConfig(singletonMap(hostConfig.getNetworkMode(), containerNetwork))
            );
        }

        return super.exec();
    }

    @Override
    public com.github.dockerjava.api.model.NetworkingConfig getNetworkingConfig() {
        return spec.getNetworkingConfig();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Deprecated
    public static class NetworkingConfig {
        @JsonProperty("EndpointsConfig")
        public Map<String, ContainerNetwork> endpointsConfig;

        public Map<String, ContainerNetwork> getEndpointsConfig() {
            return endpointsConfig;
        }

        public NetworkingConfig withEndpointsConfig(Map<String, ContainerNetwork> endpointsConfig) {
            this.endpointsConfig = endpointsConfig;
            return this;
        }
    }
}
