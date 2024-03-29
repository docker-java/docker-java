package com.github.dockerjava.core.command;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CreateVolumeCmd;
import com.github.dockerjava.api.command.CreateVolumeResponse;

/**
 * Create a volume.
 *
 * @author Marcus Linke
 */
public class CreateVolumeCmdImpl extends AbstrDockerCmd<CreateVolumeCmd, CreateVolumeResponse> implements
        CreateVolumeCmd {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Driver")
    private String driver;

    @JsonProperty("DriverOpts")
    private Map<String, String> driverOpts;

    public CreateVolumeCmdImpl(CreateVolumeCmd.Exec exec) {
        super(exec);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    public String getDriver() {
        return driver;
    }

    @Override
    public Map<String, String> getDriverOpts() {
        return driverOpts;
    }

    @Override
    public CreateVolumeCmdImpl withName(String name) {
        Objects.requireNonNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
    public CreateVolumeCmdImpl withLabels(Map<String, String> labels) {
        Objects.requireNonNull(labels, "labels was not specified");
        this.labels = labels;
        return this;
    }

    @Override
    public CreateVolumeCmdImpl withDriver(String driver) {
        Objects.requireNonNull(driver, "driver was not specified");
        this.driver = driver;
        return this;
    }

    @Override
    public CreateVolumeCmd withDriverOpts(Map<String, String> driverOpts) {
        Objects.requireNonNull(driverOpts, "driverOpts was not specified");
        this.driverOpts = driverOpts;
        return this;
    }
}
