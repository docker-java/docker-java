package com.github.dockerjava.core.command;

import java.util.Objects;

import com.github.dockerjava.api.command.RemoveConfigCmd;
import com.github.dockerjava.api.exception.NotFoundException;

/**
 * Remove a config.
 */
public class RemoveConfigCmdImpl extends AbstrDockerCmd<RemoveConfigCmd, Void> implements RemoveConfigCmd {

    private String configId;

    public RemoveConfigCmdImpl(Exec exec, String configId) {
        super(exec);
        withConfigId(configId);
    }

    @Override
    public String getConfigId() {
        return configId;
    }

    @Override
    public RemoveConfigCmd withConfigId(String configId) {
        this.configId = Objects.requireNonNull(configId, "configId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such secret
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
