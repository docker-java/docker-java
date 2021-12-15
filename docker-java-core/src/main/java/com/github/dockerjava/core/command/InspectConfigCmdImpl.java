package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.InspectConfigCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Config;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(configId, "configId was not specified");
        this.configId = configId;
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
