package com.github.dockerjava.core.command;


import java.util.Objects;

import com.github.dockerjava.api.command.UpdateSwarmCmd;
import com.github.dockerjava.api.model.SwarmSpec;

/**
 * Update a swarm.
 */
public class UpdateSwarmCmdImpl extends AbstrDockerCmd<UpdateSwarmCmd, Void> implements
        UpdateSwarmCmd {

    private Long version;
    private Boolean rotateWorkerToken;
    private Boolean rotateManagerToken;
    private SwarmSpec swarmSpec;

    public UpdateSwarmCmdImpl(Exec exec, SwarmSpec swarmSpec) {
        super(exec);
        this.swarmSpec = swarmSpec;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public UpdateSwarmCmd withVersion(Long version) {
        this.version = version;
        return this;
    }

    @Override
    public Boolean getRotateWorkerToken() {
        return rotateWorkerToken;
    }

    @Override
    public UpdateSwarmCmd withRotateWorkerToken(Boolean rotateWorkerToken) {
        this.rotateWorkerToken = rotateWorkerToken;
        return this;
    }

    @Override
    public Boolean getRotateManagerToken() {
        return rotateManagerToken;
    }

    @Override
    public UpdateSwarmCmd withRotateManagerToken(Boolean rotateManagerToken) {
        this.rotateManagerToken = rotateManagerToken;
        return this;
    }

    @Override
    public SwarmSpec getSwarmSpec() {
        return swarmSpec;
    }

    @Override
    public UpdateSwarmCmd withSwarmSpec(SwarmSpec swarmSpec) {
        this.swarmSpec = Objects.requireNonNull(swarmSpec, "swarmSpec was not specified");
        return this;
    }
}
