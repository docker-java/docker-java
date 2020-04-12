package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class PluginSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Mounts")
    private PluginMount mounts;

    @JsonProperty("Env")
    private String[] env;

    @JsonProperty("Args")
    private String[] args;

    @JsonProperty("Devices")
    private PluginDevice devices;

    public PluginMount getMounts() {
        return mounts;
    }

    public PluginSetting withName(PluginMount mounts) {
        this.mounts = mounts;
        return this;
    }

    public String[] getEnv() {
        return env;
    }

    public PluginSetting withEnv(String[] env) {
        this.env = env;
        return this;
    }


    public String[] getArgs() {
        return args;
    }

    public PluginSetting withArgs(String[] args) {
        this.args = args;
        return this;
    }

    public PluginDevice getDevices() {
        return devices;
    }

    public PluginSetting withName(PluginDevice devices) {
        this.devices = devices;
        return this;
    }
}
