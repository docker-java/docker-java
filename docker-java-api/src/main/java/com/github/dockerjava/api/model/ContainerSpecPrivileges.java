package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

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
    public String
    toString() {
        return "ContainerSpecPrivileges{" +
                "credentialSpec=" + credentialSpec +
                ", seLinuxContext=" + seLinuxContext +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerSpecPrivileges that = (ContainerSpecPrivileges) o;
        return Objects.equals(credentialSpec, that.credentialSpec) &&
                Objects.equals(seLinuxContext, that.seLinuxContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialSpec, seLinuxContext);
    }
}
