package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.RemoveSwarmNodeCmd;
import com.github.dockerjava.api.exception.NotFoundException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Remove a container.
 */
public class RemoveSwarmNodeCmdImpl extends AbstrDockerCmd<RemoveSwarmNodeCmd, Void> implements RemoveSwarmNodeCmd {

    private String swarmNodeId;

    private Boolean force;

    public RemoveSwarmNodeCmdImpl(RemoveSwarmNodeCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
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
    public RemoveSwarmNodeCmd withContainerId(@Nonnull String swarmNodeId) {
        checkNotNull(swarmNodeId, "swarmNodeId was not specified");
        this.swarmNodeId = swarmNodeId;
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
