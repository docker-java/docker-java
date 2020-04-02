package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.CreateConfigCmd;
import com.github.dockerjava.api.command.CreateConfigResponse;
import com.github.dockerjava.api.model.ConfigSpec;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates a new secret
 */
public class CreateConfigCmdImpl extends AbstrDockerCmd<CreateConfigCmd, CreateConfigResponse> implements
        CreateConfigCmd {

    private ConfigSpec configSpec;

    public CreateConfigCmdImpl(Exec exec, ConfigSpec configSpec) {
        super(exec);
        checkNotNull(configSpec, "configSpec was not specified");
        withConfigSpec(configSpec);
    }

    @Override
    public ConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public CreateConfigCmd withConfigSpec(ConfigSpec configSpec) {
        checkNotNull(configSpec, "secretSpec was not specified");
        this.configSpec = configSpec;
        return this;
    }
}
