package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.UpdateServiceCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ServiceSpec;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class UpdateServiceCmdImpl extends AbstrDockerCmd<UpdateServiceCmd, Void>
        implements UpdateServiceCmd {

    /**
     * @since 1.24
     */
    private String serviceId;

    /**
     * @since 1.24
     */
    private ServiceSpec serviceSpec;

    /**
     * @since 1.24
     */
    private Long version;

    public UpdateServiceCmdImpl(Exec exec, String serviceId, ServiceSpec serviceSpec) {
        super(exec);
        withServiceId(serviceId);
        withServiceSpec(serviceSpec);
    }

    /**
     * @see #serviceId
     */
    @CheckForNull
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @see #serviceId
     */
    public UpdateServiceCmd withServiceId(@Nonnull String serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    /**
     * @see #serviceSpec
     */
    @CheckForNull
    public ServiceSpec getServiceSpec() {
        return serviceSpec;
    }

    /**
     * @see #serviceSpec
     */
    public UpdateServiceCmd withServiceSpec(ServiceSpec serviceSpec) {
        this.serviceSpec = serviceSpec;
        return this;
    }

    /**
     * @see #version
     */
    @CheckForNull
    public Long getVersion() {
        return version;
    }

    /**
     * @see #version
     */
    public UpdateServiceCmdImpl withVersion(Long version) {
        this.version = version;
        return this;
    }

    /**
     * @throws NotFoundException No such service
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
