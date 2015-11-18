package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;

/**
 * Return Docker server info
 */
public class InfoCmdImpl extends AbstrDockerCmd<InfoCmd, Info> implements InfoCmd {

    public InfoCmdImpl(InfoCmd.Exec exec) {
        super(exec);
    }

}
