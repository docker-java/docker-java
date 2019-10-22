package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

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
        return "ContainerSpecPrivilegesSELinuxContext{" +
                "disable=" + disable +
                ", user='" + user + '\'' +
                ", role='" + role + '\'' +
                ", type='" + type + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerSpecPrivilegesSELinuxContext that = (ContainerSpecPrivilegesSELinuxContext) o;
        return Objects.equals(disable, that.disable) &&
                Objects.equals(user, that.user) &&
                Objects.equals(role, that.role) &&
                Objects.equals(type, that.type) &&
                Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disable, user, role, type, level);
    }
}
