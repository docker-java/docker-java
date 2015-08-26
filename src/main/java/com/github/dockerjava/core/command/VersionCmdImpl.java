package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.model.Version;

/**
 * Returns the Docker version info.
 */
public class VersionCmdImpl extends AbstrDockerCmd<VersionCmd, Version> implements VersionCmd {

    public VersionCmdImpl(VersionCmd.Exec exec) {
        super(exec);
    }
}
