package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.InspectSwarmNodeCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.SwarmNode;

import javax.annotation.CheckForNull;

import java.util.Objects;

/**
 * Inspect the details of a swarmNode.
 */
public class InspectSwarmNodeCmdImpl extends AbstrDockerCmd<InspectSwarmNodeCmd, SwarmNode> implements
        InspectSwarmNodeCmd {

    private String swarmNodeId;

    public InspectSwarmNodeCmdImpl(Exec exec, String swarmNodeId) {
        super(exec);
        withSwarmNodeId(swarmNodeId);
    }

    @Override
    @CheckForNull
    public String getSwarmNodeId() {
        return swarmNodeId;
    }

    @Override
    public InspectSwarmNodeCmd withSwarmNodeId(String swarmNodeId) {
        this.swarmNodeId = Objects.requireNonNull(swarmNodeId, "swarmNodeId was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such swarmNode
     */
    @Override
    public SwarmNode exec() throws NotFoundException {
        return super.exec();
    }
}
