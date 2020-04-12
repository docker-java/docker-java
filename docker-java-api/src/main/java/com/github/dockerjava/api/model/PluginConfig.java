package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class PluginConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("DockerVersion")
    private String dockerVersion;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Documentation")
    private String documentation;

    @JsonProperty("Interface")
    private Interface anInterface;

    @JsonProperty("Entrypoint")
    private String[] entrypoint;

    @JsonProperty("WorkDir")
    private String workDir;

    @JsonProperty("User")
    private User user;

    @JsonProperty("Network")
    private Network network;

    @JsonProperty("Linux")
    private Linux linux;

    @JsonProperty("PropagatedMount")
    private String propagatedMount;

    @JsonProperty("IpcHost")
    private Boolean ipcHost;

    @JsonProperty("PidHost")
    private Boolean pidHost;

    @JsonProperty("Mounts")
    private PluginMount mount;

    @JsonProperty("Env")
    private Env env;

    @JsonProperty("Args")
    private Args args;

    @JsonProperty("rootfs")
    private RootFs rootFs;

    public String getDockerVersion() {
        return dockerVersion;
    }

    public PluginConfig withDockerVersion(String dockerVersion) {
        this.dockerVersion = dockerVersion;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PluginConfig withDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDocumentation() {
        return documentation;
    }

    public PluginConfig withDocumentation(String documentation) {
        this.documentation = documentation;
        return this;
    }

    public Interface getAnInterface() {
        return anInterface;
    }

    public PluginConfig withAnInterface(Interface anInterface) {
        this.anInterface = anInterface;
        return this;
    }

    public String[] getEntrypoint() {
        return entrypoint;
    }

    public PluginConfig withEntrypoint(String[] entrypoint) {
        this.entrypoint = entrypoint;
        return this;
    }

    public String getWorkDir() {
        return workDir;
    }

    public PluginConfig withWorkDir(String workDir) {
        this.workDir = workDir;
        return this;
    }

    public User getUser() {
        return user;
    }

    public PluginConfig withUser(User user) {
        this.user = user;
        return this;
    }

    public Network getNetwork() {
        return network;
    }

    public PluginConfig withNetwork(Network network) {
        this.network = network;
        return this;
    }

    public Linux getLinux() {
        return linux;
    }

    public PluginConfig withLinux(Linux linux) {
        this.linux = linux;
        return this;
    }

    public String getPropagatedMount() {
        return propagatedMount;
    }

    public PluginConfig withPropagatedMount(String propagatedMount) {
        this.propagatedMount = propagatedMount;
        return this;
    }

    public Boolean getIpcHost() {
        return ipcHost;
    }

    public PluginConfig withIpcHost(Boolean ipcHost) {
        this.ipcHost = ipcHost;
        return this;
    }

    public Boolean getPidHost() {
        return pidHost;
    }

    public PluginConfig withPidHost(Boolean pidHost) {
        this.pidHost = pidHost;
        return this;
    }

    public PluginMount getMount() {
        return mount;
    }

    public PluginConfig withMount(PluginMount mount) {
        this.mount = mount;
        return this;
    }

    public Env getEnv() {
        return env;
    }

    public PluginConfig withEnv(Env env) {
        this.env = env;
        return this;
    }

    public Args getArgs() {
        return args;
    }

    public PluginConfig withArgs(Args args) {
        this.args = args;
        return this;
    }

    public RootFs getRootFs() {
        return rootFs;
    }

    public PluginConfig withRootFs(RootFs rootFs) {
        this.rootFs = rootFs;
        return this;
    }

    public class Interface implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("Types")
        private String[] types;

        @JsonProperty("Socket")
        private String socket;

        @JsonProperty("ProtocolScheme")
        private String protocolScheme;

        public String[] getTypes() {
            return types;
        }

        public Interface withTypes(String[] types) {
            this.types = types;
            return this;
        }

        public String getSocket() {
            return socket;
        }

        public Interface withSocket(String socket) {
            this.socket = socket;
            return this;
        }

        public String getProtocolScheme() {
            return protocolScheme;
        }

        public Interface withProtocolScheme(String protocolScheme) {
            this.protocolScheme = protocolScheme;
            return this;
        }
    }

    public class User implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("UID")
        private Integer uid;

        @JsonProperty("GID")
        private Integer gid;

        public Integer getUid() {
            return uid;
        }

        public User withUid(Integer uid) {
            this.uid = uid;
            return this;
        }

        public Integer getGid() {
            return gid;
        }

        public User withGid(Integer gid) {
            this.gid = gid;
            return this;
        }
    }

    public class Network implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("Type")
        private String type;

        public String getType() {
            return type;
        }

        public Network withType(String type) {
            this.type = type;
            return this;
        }
    }

    public class Linux implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("Capabilities")
        private String[] capabilities;

        @JsonProperty("AllowAllDevices")
        private Boolean allowAllDevices;

        @JsonProperty("Devices")
        private PluginDevice device;

        public String[] getCapabilities() {
            return capabilities;
        }

        public Linux withCapabilities(String[] capabilities) {
            this.capabilities = capabilities;
            return this;
        }

        public Boolean getAllowAllDevices() {
            return allowAllDevices;
        }

        public Linux withAllowAllDevices(Boolean allowAllDevices) {
            this.allowAllDevices = allowAllDevices;
            return this;
        }

        public PluginDevice getDevice() {
            return device;
        }

        public Linux withDevice(PluginDevice device) {
            this.device = device;
            return this;
        }
    }

    public class Env implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Description")
        private String description;

        @JsonProperty("Settable")
        private String settable;

        @JsonProperty("Value")
        private String value;

        public String getName() {
            return name;
        }

        public Env withName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Env withDescription(String description) {
            this.description = description;
            return this;
        }

        public String getSettable() {
            return settable;
        }

        public Env withSettable(String settable) {
            this.settable = settable;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Env withValue(String value) {
            this.value = value;
            return this;
        }
    }

    public class Args implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Description")
        private String description;

        @JsonProperty("Settable")
        private String[] settable;

        @JsonProperty("Value")
        private String[] value;

        public String getName() {
            return name;
        }

        public Args withName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Args withDescription(String description) {
            this.description = description;
            return this;
        }

        public String[] getSettable() {
            return settable;
        }

        public Args withSettable(String[] settable) {
            this.settable = settable;
            return this;
        }

        public String[] getValue() {
            return value;
        }

        public Args withValue(String[] value) {
            this.value = value;
            return this;
        }
    }

    public class RootFs implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("type")
        private String type;

        @JsonProperty("diff_ids")
        private String[] diffIds;

        public String getType() {
            return type;
        }

        public RootFs withType(String type) {
            this.type = type;
            return this;
        }

        public String[] getDiffIds() {
            return diffIds;
        }

        public RootFs withName(String[] diffIds) {
            this.diffIds = diffIds;
            return this;
        }
    }
}


