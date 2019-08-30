package com.github.dockerjava.core.command;


import com.github.dockerjava.api.command.InspectSwarmCmd;
import com.github.dockerjava.api.model.Swarm;

/**
 * Inspect a swarm.
 */
public class InspectSwarmCmdImpl extends AbstrDockerCmd<InspectSwarmCmd, Swarm> implements
        InspectSwarmCmd {

    public InspectSwarmCmdImpl(InspectSwarmCmd.Exec exec) {
        super(exec);
    }

    @Override
    public Swarm exec() {
        return super.exec();
    }
}
