package com.github.dockerjava.core.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.HealthCheck;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.api.model.Volumes;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonMap;

/**
 * Creates a new container.
 * `/containers/create`
 */
public class CreateContainerCmdImpl extends AbstrDockerCmd<CreateContainerCmd, CreateContainerResponse> implements
        CreateContainerCmd {

    private String name;

    @JsonProperty("Hostname")
    private String hostName;

    @JsonProperty("Domainname")
    private String domainName;

    @JsonProperty("User")
    private String user;

    @JsonProperty("AttachStdin")
    private Boolean attachStdin;

    @JsonProperty("AttachStdout")
    private Boolean attachStdout;

    @JsonProperty("AttachStderr")
    private Boolean attachStderr;

    @JsonProperty("PortSpecs")
    private String[] portSpecs;

    @JsonProperty("Tty")
    private Boolean tty;

    @JsonProperty("OpenStdin")
    private Boolean stdinOpen;

    @JsonProperty("StdinOnce")
    private Boolean stdInOnce;

    @JsonProperty("Env")
    private String[] env;

    @JsonProperty("Cmd")
    private String[] cmd;

    @JsonProperty("Healthcheck")
    private HealthCheck healthcheck;

    @JsonProperty("ArgsEscaped")
    private Boolean argsEscaped;

    @JsonProperty("Entrypoint")
    private String[] entrypoint;

    @JsonProperty("Image")
    private String image;

    @JsonProperty("Volumes")
    private Volumes volumes = new Volumes();

    @JsonProperty("WorkingDir")
    private String workingDir;

    @JsonProperty("MacAddress")
    private String macAddress;

    @JsonProperty("OnBuild")
    private List<String> onBuild;

    @JsonProperty("NetworkDisabled")
    private Boolean networkDisabled;

    @JsonProperty("ExposedPorts")
    private ExposedPorts exposedPorts = new ExposedPorts();

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_21}
     */
    @JsonProperty("StopSignal")
    private String stopSignal;

    @JsonProperty("StopTimeout")
    private Integer stopTimeout;

    @JsonProperty("HostConfig")
    private HostConfig hostConfig = new HostConfig();

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Shell")
    private List<String> shell;

    @JsonProperty("NetworkingConfig")
    private NetworkingConfig networkingConfig;

    @JsonIgnore
    private String ipv4Address = null;

    @JsonIgnore
    private String ipv6Address = null;

    @JsonIgnore
    private List<String> aliases = null;

    private AuthConfig authConfig;

    private String platform;

    public CreateContainerCmdImpl(CreateContainerCmd.Exec exec, AuthConfig authConfig, String image) {
        super(exec);
        withAuthConfig(authConfig);
        withImage(image);
    }

    public AuthConfig getAuthConfig() {
        return authConfig;
    }

    public CreateContainerCmd withAuthConfig(AuthConfig authConfig) {
        this.authConfig = authConfig;
        return this;
    }

    @Override
    @JsonIgnore
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
        checkNotNull(aliases, "aliases was not specified");
        this.aliases = aliases;
        return this;
    }


    @Override
    public String[] getCmd() {
        return cmd;
    }

    @Override
    public CreateContainerCmd withCmd(String... cmd) {
        checkNotNull(cmd, "cmd was not specified");
        this.cmd = cmd;
        return this;
    }

    @Override
    public CreateContainerCmd withCmd(List<String> cmd) {
        checkNotNull(cmd, "cmd was not specified");
        return withCmd(cmd.toArray(new String[0]));
    }

    @CheckForNull
    public HealthCheck getHealthcheck() {
        return healthcheck;
    }

    public CreateContainerCmdImpl withHealthcheck(HealthCheck healthcheck) {
        this.healthcheck = healthcheck;
        return this;
    }

    public Boolean getArgsEscaped() {
        return argsEscaped;
    }

    public CreateContainerCmdImpl withArgsEscaped(Boolean argsEscaped) {
        this.argsEscaped = argsEscaped;
        return this;
    }

    @Override
    public String getDomainName() {
        return domainName;
    }

    @Override
    public CreateContainerCmd withDomainName(String domainName) {
        checkNotNull(domainName, "no domainName was specified");
        this.domainName = domainName;
        return this;
    }

    @Override
    public String[] getEntrypoint() {
        return entrypoint;
    }

    @Override
    public CreateContainerCmd withEntrypoint(String... entrypoint) {
        checkNotNull(entrypoint, "entrypoint was not specified");
        this.entrypoint = entrypoint;
        return this;
    }

    @Override
    public CreateContainerCmd withEntrypoint(List<String> entrypoint) {
        checkNotNull(entrypoint, "entrypoint was not specified");
        return withEntrypoint(entrypoint.toArray(new String[0]));
    }

    @Override
    public String[] getEnv() {
        return env;
    }

    @Override
    public CreateContainerCmd withEnv(String... env) {
        checkNotNull(env, "env was not specified");
        this.env = env;
        return this;
    }

    @Override
    public CreateContainerCmd withEnv(List<String> env) {
        checkNotNull(env, "env was not specified");
        return withEnv(env.toArray(new String[0]));
    }

    @Override
    @JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }

    @Override
    public CreateContainerCmd withExposedPorts(ExposedPort... exposedPorts) {
        checkNotNull(exposedPorts, "exposedPorts was not specified");
        this.exposedPorts = new ExposedPorts(exposedPorts);
        return this;
    }

    @Override
    public CreateContainerCmd withExposedPorts(List<ExposedPort> exposedPorts) {
        checkNotNull(exposedPorts, "exposedPorts was not specified");
        return withExposedPorts(exposedPorts.toArray(new ExposedPort[0]));
    }

    /**
     * @see #stopSignal
     */
    @JsonIgnore
    @Override
    public String getStopSignal() {
        return stopSignal;
    }

    @Override
    public CreateContainerCmd withStopSignal(String stopSignal) {
        checkNotNull(stopSignal, "stopSignal wasn't specified.");
        this.stopSignal = stopSignal;
        return this;
    }

    @Override
    public Integer getStopTimeout() {
        return stopTimeout;
    }

    @Override
    public CreateContainerCmd withStopTimeout(Integer stopTimeout) {
        this.stopTimeout = stopTimeout;
        return this;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public CreateContainerCmd withHostName(String hostName) {
        checkNotNull(hostName, "no hostName was specified");
        this.hostName = hostName;
        return this;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public CreateContainerCmd withImage(String image) {
        checkNotNull(image, "no image was specified");
        this.image = image;
        return this;
    }

    @Override
    @JsonIgnore
    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    public CreateContainerCmd withLabels(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.labels = labels;
        return this;
    }

    @Override
    public String getMacAddress() {
        return macAddress;
    }

    @Override
    public CreateContainerCmd withMacAddress(String macAddress) {
        checkNotNull(macAddress, "macAddress was not specified");
        this.macAddress = macAddress;
        return this;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public CreateContainerCmd withName(String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
    public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
    public CreateContainerCmd withPortSpecs(String... portSpecs) {
        checkNotNull(portSpecs, "portSpecs was not specified");
        this.portSpecs = portSpecs;
        return this;
    }

    @Override
    public CreateContainerCmd withPortSpecs(List<String> portSpecs) {
        checkNotNull(portSpecs, "portSpecs was not specified");
        return withPortSpecs(portSpecs.toArray(new String[0]));
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public CreateContainerCmd withUser(String user) {
        checkNotNull(user, "user was not specified");
        this.user = user;
        return this;
    }

    @Override
    public Boolean isAttachStderr() {
        return attachStderr;
    }

    @Override
    public CreateContainerCmd withAttachStderr(Boolean attachStderr) {
        checkNotNull(attachStderr, "attachStderr was not specified");
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
    public Boolean isAttachStdin() {
        return attachStdin;
    }

    @Override
    public CreateContainerCmd withAttachStdin(Boolean attachStdin) {
        checkNotNull(attachStdin, "attachStdin was not specified");
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
    public Boolean isAttachStdout() {
        return attachStdout;
    }

    @Override
    public CreateContainerCmd withAttachStdout(Boolean attachStdout) {
        checkNotNull(attachStdout, "attachStdout was not specified");
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
    @JsonIgnore
    public Volume[] getVolumes() {
        return volumes.getVolumes();
    }

    @Override
    public CreateContainerCmd withVolumes(Volume... volumes) {
        checkNotNull(volumes, "volumes was not specified");
        this.volumes = new Volumes(volumes);
        return this;
    }

    @Override
    public CreateContainerCmd withVolumes(List<Volume> volumes) {
        checkNotNull(volumes, "volumes was not specified");
        return withVolumes(volumes.toArray(new Volume[0]));
    }

    @Override
    public String getWorkingDir() {
        return workingDir;
    }

    @Override
    public CreateContainerCmd withWorkingDir(String workingDir) {
        checkNotNull(workingDir, "workingDir was not specified");
        this.workingDir = workingDir;
        return this;
    }

    @Override
    public Boolean isNetworkDisabled() {
        return networkDisabled;
    }

    @Override
    public CreateContainerCmd withNetworkDisabled(Boolean disableNetwork) {
        checkNotNull(disableNetwork, "disableNetwork was not specified");
        this.networkDisabled = disableNetwork;
        return this;
    }


    @Override
    public Boolean isStdInOnce() {
        return stdInOnce;
    }

    @Override
    public CreateContainerCmd withStdInOnce(Boolean stdInOnce) {
        checkNotNull(stdInOnce, "no stdInOnce was specified");
        this.stdInOnce = stdInOnce;
        return this;
    }

    @Override
    public Boolean isStdinOpen() {
        return stdinOpen;
    }

    @Override
    public CreateContainerCmd withStdinOpen(Boolean stdinOpen) {
        checkNotNull(stdinOpen, "no stdinOpen was specified");
        this.stdinOpen = stdinOpen;
        return this;
    }


    @Override
    public Boolean isTty() {
        return tty;
    }

    @Override
    public CreateContainerCmd withTty(Boolean tty) {
        checkNotNull(tty, "no tty was specified");
        this.tty = tty;
        return this;
    }

    @Override
    public HostConfig getHostConfig() {
        return hostConfig;
    }

    @Override
    public CreateContainerCmd withHostConfig(HostConfig hostConfig) {
        this.hostConfig = hostConfig;
        return this;
    }

    @Override
    public String getIpv4Address() {
        return ipv4Address;
    }

    @Override
    public CreateContainerCmd withIpv4Address(String ipv4Address) {
        checkNotNull(ipv4Address, "no ipv4Address was specified");
        this.ipv4Address = ipv4Address;
        return this;
    }

    @Override
    public String getIpv6Address() {
        return ipv6Address;
    }

    @Override
    public CreateContainerCmd withIpv6Address(String ipv6Address) {
        checkNotNull(ipv6Address, "no ipv6Address was specified");
        this.ipv6Address = ipv6Address;
        return this;
    }

    @CheckForNull
    public List<String> getOnBuild() {
        return onBuild;
    }

    public CreateContainerCmdImpl withOnBuild(List<String> onBuild) {
        this.onBuild = onBuild;
        return this;
    }

    @CheckForNull
    @Override
    public String getPlatform() {
        return platform;
    }

    @Override
    public CreateContainerCmd withPlatform(String platform) {
        this.platform = platform;
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
            networkingConfig = new NetworkingConfig()
                    .withEndpointsConfig(singletonMap(hostConfig.getNetworkMode(), containerNetwork));
        }

        return super.exec();
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
