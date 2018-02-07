package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

/**
 * SELinux labels of the container
 *
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerSpecPrivilegesSELinuxContext implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("Disable")
    private Boolean disable;

    @JsonProperty("User")
    private String user;

    @JsonProperty("Role")
    private String role;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Level")
    private String level;

    public Boolean getDisable() {
        return disable;
    }

    public ContainerSpecPrivilegesSELinuxContext withDisable(Boolean disable) {
        this.disable = disable;
        return this;
    }

    public String getUser() {
        return user;
    }

    public ContainerSpecPrivilegesSELinuxContext withUser(String user) {
        this.user = user;
        return this;
    }

    public String getRole() {
        return role;
    }

    public ContainerSpecPrivilegesSELinuxContext withRole(String role) {
        this.role = role;
        return this;
    }

    public String getType() {
        return type;
    }

    public ContainerSpecPrivilegesSELinuxContext withType(String type) {
        this.type = type;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public ContainerSpecPrivilegesSELinuxContext withLevel(String level) {
        this.level = level;
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
