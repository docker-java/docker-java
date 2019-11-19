package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * Security options for the container
 *
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
@EqualsAndHashCode
@ToString
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
}
