package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * SELinux labels of the container
 *
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
@EqualsAndHashCode
@ToString
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
}
