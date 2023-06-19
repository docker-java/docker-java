package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.InspectConfigCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Config;

/**
 * Inspect the details of a config.
 */
public class InspectConfigCmdImpl extends AbstrDockerCmd<InspectConfigCmd, Config> implements InspectConfigCmd {

    private String configId;

    public InspectConfigCmdImpl(Exec exec, String configId) {
        super(exec);
        withConfigId(configId);
    }

    @Override
    public String getConfigId() {
        return configId;
    }

    @Override
    public InspectConfigCmd withConfigId(String configId) {
        this.configId = Objects.requireNonNull(configId, "configId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such config
     */
    @Override
    public Config exec() throws NotFoundException {
        return super.exec();
    }
}
