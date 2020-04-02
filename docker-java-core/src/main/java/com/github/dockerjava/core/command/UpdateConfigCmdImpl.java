package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.UpdateConfigCmd;
import com.github.dockerjava.api.model.ConfigSpec;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates a new config
 */
public class UpdateConfigCmdImpl extends AbstrDockerCmd<UpdateConfigCmd, Void> implements
        UpdateConfigCmd {

    private String configId;
    private ConfigSpec configSpec;

    public UpdateConfigCmdImpl(Exec exec, String configId, ConfigSpec configSpec) {
        super(exec);
        checkNotNull(configSpec, "configSpec was not specified");
        checkNotNull(configId, "configId was not specified");
        withConfigSpec(configSpec);
        withConfigId(configId);
    }

    @Override
    public UpdateConfigCmd withConfigSpec(ConfigSpec configSpec) {
        checkNotNull(configSpec, "secretSpec was not specified");
        this.configSpec = configSpec;
        return this;
    }

    @Override
    public UpdateConfigCmd withConfigId(String configId) {
        checkNotNull(configId, "configId was not specified");
        this.configId = configId;
        return this;
    }

    @Override
    public ConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public String getConfigId() {
        return configId;
    }

}
