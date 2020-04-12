package com.github.dockerjava.core.command;


import com.github.dockerjava.api.command.CreatePluginCmd;
import com.github.dockerjava.api.command.CreatePluginResponse;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Creates a new plugin
 */
public class CreatePluginCmdImpl extends AbstrDockerCmd<CreatePluginCmd, CreatePluginResponse> implements
        CreatePluginCmd {


    private String name;

    public CreatePluginCmdImpl(Exec exec, String name) {
        super(exec);
        checkNotNull(name, "name was not specified");
        withName(name);
    }

    @CheckForNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public CreatePluginCmd withName(@Nonnull String name) {
        this.name = name;
        return this;
    }
}

