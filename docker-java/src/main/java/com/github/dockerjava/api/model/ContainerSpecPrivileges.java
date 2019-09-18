package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Security options for the container
 *
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
public class ContainerSpecPrivileges implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("CredentialSpec")
    private ContainerSpecPrivilegesCredential credentialSpec;

    @JsonProperty("SELinuxContext")
    private ContainerSpecPrivilegesSELinuxContext seLinuxContext;

    public ContainerSpecPrivilegesCredential getCredentialSpec() {
        return credentialSpec;
    }

    public ContainerSpecPrivileges withCredentialSpec(ContainerSpecPrivilegesCredential credentialSpec) {
        this.credentialSpec = credentialSpec;
        return this;
    }

    public ContainerSpecPrivilegesSELinuxContext getSeLinuxContext() {
        return seLinuxContext;
    }

    public ContainerSpecPrivileges withSeLinuxContext(ContainerSpecPrivilegesSELinuxContext seLinuxContext) {
        this.seLinuxContext = seLinuxContext;
        return this;
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
