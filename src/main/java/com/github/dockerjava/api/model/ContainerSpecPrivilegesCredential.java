package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * Credential for managed service account (Windows only)
 *
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
public class ContainerSpecPrivilegesCredential implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Load credential spec from this file. The file is read by the daemon, and must be present in the
     * `CredentialSpecs` subdirectory in the docker data directory, which defaults to
     * <code>C:\ProgramData\Docker\</code> on Windows.
     */
    @JsonProperty("File")
    private String file;

    /**
     * Load credential spec from this value in the Windows registry. The specified registry value must be
     * located in:
     * <code>
     * HKLM\SOFTWARE\Microsoft\Windows NT\CurrentVersion\Virtualization\Containers\CredentialSpecs
     * </code>
     */
    @JsonProperty("Registry")
    private String registry;

    public String getFile() {
        return file;
    }

    public ContainerSpecPrivilegesCredential withFile(String file) {
        this.file = file;
        return this;
    }

    public String getRegistry() {
        return registry;
    }

    public ContainerSpecPrivilegesCredential withRegistry(String registry) {
        this.registry = registry;
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
