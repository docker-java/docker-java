package com.github.dockerjava.core.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateConfigCmd;
import com.github.dockerjava.api.command.CreateConfigResponse;

import java.util.Base64;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates a new config
 */
public class CreateConfigCmdImpl extends AbstrDockerCmd<CreateConfigCmd, CreateConfigResponse> implements CreateConfigCmd {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Data")
    private String data;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public Map<String, String> getLabels() {
        return labels;
    }

    public CreateConfigCmdImpl(CreateConfigCmd.Exec exec) {
        super(exec);
    }

    @Override
    public CreateConfigCmd withName(String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
    public CreateConfigCmd withData(byte[] data) {
        checkNotNull(data, "data was not specified");
        this.data = Base64.getEncoder().encodeToString(data);
        return this;
    }

    @Override
    public CreateConfigCmd withLabels(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.labels = labels;
        return this;
    }

}
