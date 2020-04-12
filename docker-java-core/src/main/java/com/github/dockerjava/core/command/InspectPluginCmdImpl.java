package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.InspectPluginCmd;
import com.github.dockerjava.api.command.InspectPluginResponse;
import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Inspect the details of a plugin.
 */
public class InspectPluginCmdImpl extends AbstrDockerCmd<InspectPluginCmd, InspectPluginResponse> implements
        InspectPluginCmd {

    private String name;

    public InspectPluginCmdImpl(Exec exec, String name) {
        super(exec);
        withPluginName(name);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public InspectPluginCmd withPluginName(@Nonnull String name) {
        checkNotNull(name, "pluginId was not specified");
        this.name = name;
        return this;
    }


    /**
     * @throws NotFoundException No such plugin
     */
    @Override
    public InspectPluginResponse exec() throws NotFoundException {
        return super.exec();
    }
}
