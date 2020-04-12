package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RemovePluginCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Remove a config.
 */
public class RemovePluginCmdImpl extends AbstrDockerCmd<RemovePluginCmd, Void> implements RemovePluginCmd {

    private String name;
    private Boolean force;

    public RemovePluginCmdImpl(Exec exec, String name) {
        super(exec);
        withPluginName(name);
    }

    @Override
    public String getPluginName() {
        return name;
    }

    @Override
    public Boolean hasForceEnabled() {
        return force;
    }

    @Override
    public RemovePluginCmd withPluginName(String name) {
        checkNotNull(name, "name was not specified");
        this.name = name;
        return this;
    }

    @Override
    public RemovePluginCmd withForce(Boolean force) {
        this.force = force;
        return this;
    }

    /**
     * @throws NotFoundException No such Plugin
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
