package com.github.dockerjava.core.command;


import com.github.dockerjava.api.command.LeaveSwarmCmd;

import javax.annotation.CheckForNull;

public class LeaveSwarmCmdImpl extends AbstrDockerCmd<LeaveSwarmCmd, Void> implements LeaveSwarmCmd {

    private Boolean force;

    public LeaveSwarmCmdImpl(LeaveSwarmCmd.Exec exec) {
        super(exec);
    }

    @Override
    @CheckForNull
    public Boolean hasForceEnabled() {
        return force;
    }

    @Override
    public LeaveSwarmCmd withForceEnabled(Boolean force) {
        this.force = force;
        return this;
    }

    @Override
    public Void exec() {
        return super.exec();
    }


}
