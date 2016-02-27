package com.github.dockerjava.core.command;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateNetworkCmd;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.command.DockerCmdSyncExec;
import com.github.dockerjava.api.model.Network;
import com.github.dockerjava.api.model.Network.Ipam;

public class CreateNetworkCmdImpl extends AbstrDockerCmd<CreateNetworkCmd, CreateNetworkResponse>
        implements CreateNetworkCmd {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("IPAM")
    private Network.Ipam ipam;

    @JsonProperty("Options")
    private Map<String, String> options = new HashMap<>();

    @JsonProperty("CheckDuplicate")
    private Boolean checkDuplicate;

    @JsonProperty("Internal")
    private Boolean internal;

    @JsonProperty("EnableIPv6")
    private Boolean enableIpv6;

    public CreateNetworkCmdImpl(DockerCmdSyncExec<CreateNetworkCmd, CreateNetworkResponse> execution) {
        super(execution);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public Network.Ipam getIpam() {
        return ipam;
    }

    @Override
    public Map<String, String> getOptions() {
        return options;
    }

    @Override
    public Boolean getCheckDuplicate() {
        return checkDuplicate;
    }

    @Override
    public Boolean getInternal() {
        return internal;
    }

    @Override
    public Boolean getEnableIPv6() {
        return enableIpv6;
    }

    @Override
    public CreateNetworkCmd withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CreateNetworkCmd withDriver(String driver) {
        this.driver = driver;
        return this;
    }

    @Override
    public CreateNetworkCmd withIpam(Ipam ipam) {
        this.ipam = ipam;
        return this;
    }

    @Override
    public CreateNetworkCmd withOptions(Map<String, String> options) {
        this.options = options;
        return this;
    }

    @Override
    public CreateNetworkCmd withCheckDuplicate(boolean checkDuplicate) {
        this.checkDuplicate = checkDuplicate;
        return this;
    }

    @Override
    public CreateNetworkCmd withInternal(boolean internal) {
        this.internal = internal;
        return this;
    }

    @Override
    public CreateNetworkCmd withEnableIpv6(boolean enableIpv6) {
        this.enableIpv6 = enableIpv6;
        return this;
    }
}
