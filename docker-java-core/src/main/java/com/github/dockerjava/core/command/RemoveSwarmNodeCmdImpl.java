package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RemoveSwarmNodeCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import java.util.Objects;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Remove a container.
 */
public class RemoveSwarmNodeCmdImpl extends AbstrDockerCmd<RemoveSwarmNodeCmd, Void> implements RemoveSwarmNodeCmd {

    private String swarmNodeId;

    private Boolean force;

    public RemoveSwarmNodeCmdImpl(RemoveSwarmNodeCmd.Exec exec, String swarmNodeId) {
        super(exec);
        withSwarmNodeId(swarmNodeId);
    }

    @Override
    @CheckForNull
    public String getSwarmNodeId() {
        return swarmNodeId;
    }

    @Override
    @CheckForNull
    public Boolean hasForceEnabled() {
        return force;
    }

    @Override
    public RemoveSwarmNodeCmd withSwarmNodeId(@Nonnull String swarmNodeId) {
        this.swarmNodeId = Objects.requireNonNull(swarmNodeId, "swarmNodeId was not specified");
        return this;
    }

    @Override
    public RemoveSwarmNodeCmd withForce(Boolean force) {
        this.force = force;
        return this;
    }

    /**
     * @throws NotFoundException No such swarmNode
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }
}
