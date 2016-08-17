package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.UpdateSwarmNodeCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.SwarmNodeSpec;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Update swarmNode spec
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class UpdateSwarmNodeCmdImpl extends AbstrDockerCmd<UpdateSwarmNodeCmd, Void>
        implements UpdateSwarmNodeCmd {

    private String swarmNodeId;

    private SwarmNodeSpec swarmNodeSpec;

    public UpdateSwarmNodeCmdImpl(Exec exec, String swarmNodeId, SwarmNodeSpec swarmNodeSpec) {
        super(exec);
        withSwarmNodeId(swarmNodeId);
        withSwarmNodeSpec(swarmNodeSpec);
    }

    /**
     * @see #swarmNodeId
     */
    @CheckForNull
    public String getSwarmNodeId() {
        return swarmNodeId;
    }

    /**
     * @see #swarmNodeId
     */
    public UpdateSwarmNodeCmd withSwarmNodeId(@Nonnull String swarmNodeId) {
        this.swarmNodeId = swarmNodeId;
        return this;
    }

    /**
     * @see #swarmNodeSpec
     */
    @CheckForNull
    public SwarmNodeSpec getSwarmNodeSpec() {
        return swarmNodeSpec;
    }

    /**
     * @see #swarmNodeSpec
     */
    public UpdateSwarmNodeCmd withSwarmNodeSpec(SwarmNodeSpec swarmNodeSpec) {
        checkNotNull(swarmNodeSpec.getAvailability(), "Availability in SwarmNodeSpec was not specified");
        checkNotNull(swarmNodeSpec.getRole(), "Role in SwarmNodeSpec was not specified");
        this.swarmNodeSpec = swarmNodeSpec;
        return this;
    }

    /**
     * @throws NotFoundException No such swarmNode
     */
    @Override
    public Void exec() throws NotFoundException {
        return super.exec();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
